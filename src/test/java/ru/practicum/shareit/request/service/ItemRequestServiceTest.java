package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestBaseTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {"db.name=test"})
class ItemRequestServiceTest extends ItemRequestBaseTest {
    @Autowired
    private ItemRequestService itemRequestService;
    @MockBean
    private ItemRequestMapper itemRequestMapper;
    @MockBean
    private ItemMapper itemMapper;
    @MockBean
    private ItemRequestRepository itemRequestRepository;
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private UserRepository userRepository;


    @BeforeEach
    protected void setUp() {
        super.setUp();

        Mockito.when(itemRequestMapper.convertToEntity(Mockito.any(ItemRequestDto.class))).thenReturn(itemRequest1);
        Mockito.when(itemRequestMapper.convertToDto(Mockito.any(ItemRequest.class))).thenReturn(itemRequestDto1);
        Mockito.when(itemMapper.convertRequestDtoToEntity(Mockito
                .any(ru.practicum.shareit.item.dto.ItemRequestDto.class))).thenReturn(item1);
        Mockito.when(itemMapper.convertToResponseDto(Mockito.any(Item.class))).thenReturn(itemResponseDto1);
    }

    @Test
    void shouldAddNewItemRequest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));
        Mockito.when(itemRequestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(itemRequest1);
        ItemRequestDto itemRequestDto = itemRequestService.addNewItemRequest(user1.getId(), itemRequestDto1);

        assertNotNull(itemRequestDto);
        assertEquals(itemRequestDto, itemRequestDto1);
        Mockito.verify(itemRequestRepository, Mockito.times(1)).save(itemRequest1);
    }

    @Test
    void shouldGetOwnerItemRequests() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));
        Mockito.when(itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(Mockito.anyLong()))
                .thenReturn(List.of(itemRequest1));
        Mockito.when(itemRepository.findAllByRequestIn(Mockito.anyCollection()))
                .thenReturn(List.of(item1));
        Collection<ItemRequestDto> itemRequests = itemRequestService.getOwnerItemRequests(itemRequest1.getId());

        assertNotNull(itemRequests);
        assertEquals(1, itemRequests.size());
        assertEquals(itemRequests, List.of(itemRequestDto1));
        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .findAllByRequestorIdOrderByCreatedDesc(1L);
    }

    @Test
    void shouldGetAllItemRequests() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));
        Mockito.when(itemRequestRepository.findAllByRequestorIdNotOrderByCreatedDesc(Mockito.anyLong(),
                        Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(itemRequest1)));
        Mockito.when(itemRepository.findAllByRequestIn(Mockito.anyCollection()))
                .thenReturn(List.of(item1));
        Collection<ItemRequestDto> itemRequests = itemRequestService.getAllItemRequests(itemRequest1.getId(), 0,
                10);

        assertNotNull(itemRequests);
        assertEquals(1, itemRequests.size());
        assertEquals(itemRequests, List.of(itemRequestDto1));
        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .findAllByRequestorIdNotOrderByCreatedDesc(1L, PageRequest.of(0, 10,
                        Sort.by("created").descending()));
    }

    @Test
    void shouldGetItemRequest() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest1));
        Mockito.when(itemRepository.findAllByRequestId(Mockito.anyLong())).thenReturn(List.of(item1));
        ItemRequestDto itemRequestDto = itemRequestService.getItemRequest(user1.getId(), itemRequest1.getId());

        assertNotNull(itemRequestDto);
        assertEquals(itemRequestDto, itemRequestDto1);
        Mockito.verify(itemRequestRepository, Mockito.times(1)).findById(1L);
    }
}