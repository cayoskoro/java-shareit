package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.exception.IncorrectParameterException;
import ru.practicum.shareit.common.exception.NotAvailableException;
import ru.practicum.shareit.item.ItemBaseTest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"db.name=test"})
class ItemServiceTest extends ItemBaseTest {
    @Autowired
    private ItemService itemService;
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BookingRepository bookingRepository;
    @MockBean
    private ItemRequestRepository itemRequestRepository;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private ItemMapper itemMapper;
    @MockBean
    private CommentMapper commentMapper;
    @MockBean
    private BookingMapper bookingMapper;

    @BeforeEach
    protected void setUp() {
        super.setUp();

        Mockito.when(commentMapper.convertToEntity(Mockito.any(CommentDto.class))).thenReturn(comment1);
        Mockito.when(commentMapper.convertToDto(Mockito.any(Comment.class))).thenReturn(commentDto1);
        Mockito.when(commentMapper.convertToDtoCollection(Mockito.anyCollection()))
                .thenReturn(Collections.singletonList(commentDto1));

        Mockito.when(itemMapper.convertRequestDtoToEntity(
                Mockito.any(ru.practicum.shareit.item.dto.ItemRequestDto.class))).thenReturn(item1);
        Mockito.when(itemMapper.convertToResponseDto(Mockito.any(Item.class))).thenReturn(itemResponseDto1);
        Mockito.when(itemMapper.clone(Mockito.any(Item.class))).thenReturn(item1);
        Mockito.when(itemMapper.convertToResponseDtoCollection(Mockito.anyCollection()))
                .thenReturn(Collections.singletonList(itemResponseDto1));

        Mockito.when(bookingMapper.convertToResponseDto(Mockito.any(Booking.class)))
                .thenReturn(bookingResponseDto1);
        Mockito.when(bookingMapper.convertToResponseShortDto(Mockito.any(Booking.class)))
                .thenReturn(bookingResponseShortDto1);
        Mockito.when(bookingMapper.convertRequestDtoToEntity(Mockito.any(BookingRequestDto.class)))
                .thenReturn(booking1);
        Mockito.when(bookingMapper.convertToResponseDtoCollection(Mockito.anyCollection()))
                .thenReturn(Collections.singletonList(bookingResponseDto1));
    }

    @Test
    void shouldGetAllUserItems() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findAllByOwnerIdOrderByIdAsc(Mockito.anyLong(), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(item1)));
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndItemIn(Mockito.anyLong(), Mockito.anyCollection()))
                .thenReturn(List.of(booking1, booking2));
        comment1.setItem(item1);
        Mockito.when(commentRepository.findAllByItemInEager(Mockito.anyCollection()))
                .thenReturn(List.of(comment1));

        Collection<ItemResponseDto> items = itemService.getAllUserItems(user1.getId(), 0, 10);

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(items, List.of(itemResponseDto1.toBuilder().nextBooking(bookingResponseShortDto1).build()));
        Mockito.verify(itemRepository, Mockito.times(1))
                .findAllByOwnerIdOrderByIdAsc(1L, PageRequest.of(0, 10));
    }

    @Test
    void shouldGetItemById() {
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item1));
        Mockito.when(bookingRepository.findAllByItemOwnerId(Mockito.anyLong()))
                .thenReturn(List.of(booking1, booking2));
        comment1.setItem(item1);
        Mockito.when(commentRepository.findAllByItemIdEager(Mockito.anyLong())).thenReturn(List.of(comment1));

        ItemResponseDto itemResponseDto = itemService.getItemById(user1.getId(), item1.getId());

        assertNotNull(itemResponseDto);
        assertEquals(itemResponseDto, itemResponseDto1.toBuilder().nextBooking(bookingResponseShortDto1).build());
        Mockito.verify(itemRepository, Mockito.times(1))
                .findById(1L);
    }

    @Test
    void shouldAddNewItem() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(itemRequest1));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item1);

        itemRequestDto1 = itemRequestDto1.toBuilder().requestId(null).build();
        ItemResponseDto itemResponseDto = itemService.addNewItem(user1.getId(), itemRequestDto1);

        assertNotNull(itemResponseDto);
        assertEquals(itemResponseDto, itemResponseDto1);
        Mockito.verify(itemRepository, Mockito.times(1))
                .save(item1);
    }

    @Test
    void shouldAddNewItemWithNotAvailable() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(itemRequest1));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item1);

        final NotAvailableException exception = assertThrows(
                NotAvailableException.class,
                () -> itemService.addNewItem(user1.getId(), itemRequestDto1)
        );
    }

    @Test
    void shouldEditItem() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item1));
        Mockito.doNothing().when(itemMapper)
                .updateItemFromItemRequestDto(Mockito.any(ItemRequestDto.class), Mockito.any(Item.class));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item1);

        ItemResponseDto itemResponseDto = itemService.editItem(user1.getId(), item1.getId(), itemRequestDto1);

        assertNotNull(itemResponseDto);
        assertEquals(itemResponseDto, itemResponseDto1);
        Mockito.verify(itemRepository, Mockito.times(1))
                .save(item1);
    }

    @Test
    void shouldSearchItems() {
        Mockito.when(itemRepository.searchAvailableByNameOrDescriptionContainingIgnoreCase(Mockito.anyString(),
                        Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(item1)));

        Collection<ItemResponseDto> itemResponseDtos = itemService.searchItems(item1.getName(), 0, 10);

        assertNotNull(itemResponseDtos);
        assertEquals(1, itemResponseDtos.size());
        assertEquals(itemResponseDtos, List.of(itemResponseDto1));
        Mockito.verify(itemRepository, Mockito.times(1))
                .searchAvailableByNameOrDescriptionContainingIgnoreCase(item1.getName(),
                        PageRequest.of(0, 10));
    }

    @Test
    void shouldSearchItemsWithIncorrectParameter() {
        Mockito.when(itemRepository.searchAvailableByNameOrDescriptionContainingIgnoreCase(Mockito.anyString(),
                        Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(item1)));

        final IncorrectParameterException exception = assertThrows(
                IncorrectParameterException.class,
                () -> itemService.searchItems("%%%", 0, 10)
        );
    }

    @Test
    void shouldAddNewComment() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item1));
        Mockito.when(bookingRepository.existsByBookerIdAndItemIdAndStatusAndEndIsBeforeOrderByEndDesc(Mockito.anyLong(),
                        Mockito.anyLong(), Mockito.any(Status.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(true);
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment1);

        CommentDto commentDto = itemService.addNewComment(user1.getId(), item1.getId(), commentDto1);

        assertNotNull(commentDto);
        assertEquals(commentDto, commentDto1);
        Mockito.verify(commentRepository, Mockito.times(1))
                .save(comment1);
    }

    @Test
    void shouldAddNewCommentWithNotAvailable() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item1));
        Mockito.when(bookingRepository.existsByBookerIdAndItemIdAndStatusAndEndIsBeforeOrderByEndDesc(Mockito.anyLong(),
                        Mockito.anyLong(), Mockito.any(Status.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(false);

        final NotAvailableException exception = assertThrows(
                NotAvailableException.class,
                () -> itemService.addNewComment(user1.getId(), item1.getId(), commentDto1)
        );
    }
}