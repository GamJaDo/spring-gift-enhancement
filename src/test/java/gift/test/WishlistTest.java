package gift.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import gift.controller.WishlistController;
import gift.entity.Category;
import gift.entity.Product;
import gift.entity.User;
import gift.entity.Wishlist;
import gift.service.WishlistService;

public class WishlistTest {

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private WishlistController wishlistController;

    @Mock
    private BindingResult bindingResult;

    private User user;
    private Product product;
    private Wishlist wishlist;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User("test@test.com", "pw");
        category = new Category("교환권", "#6c95d1", "https://example.com/image.jpg", "");
        product = new Product("아이스 아메리카노 T", 4500, "https://example.com/image.jpg", category);
        wishlist = new Wishlist(user, product);

        when(wishlistService.getWishlist(any(String.class), any(BindingResult.class), any(Pageable.class)))
        		.thenReturn(new PageImpl<>(List.of(wishlist), PageRequest.of(0, 10), 1));
        doNothing().when(wishlistService).addWishlist(any(String.class), any(Wishlist.class), any(BindingResult.class));
        doNothing().when(wishlistService).removeWishlist(any(String.class), any(Wishlist.class), any(BindingResult.class));
        doNothing().when(wishlistService).updateWishlistQuantity(any(String.class), any(Wishlist.class), any(BindingResult.class));
    }

    @Test
    public void testGetWishlist() {
    	Pageable pageable = PageRequest.of(0, 10);
        ResponseEntity<Page<Wishlist>> response = wishlistController.getWishlist("Bearer token", bindingResult, pageable);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent().get(0).getProduct().getName()).isEqualTo(product.getName());
    }

    @Test
    public void testAddWishlist() {
        ResponseEntity<Void> response = wishlistController.addWishlist("Bearer token", wishlist, bindingResult);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    public void testRemoveWishlist() {
        ResponseEntity<Void> response = wishlistController.removeWishlist("Bearer token", wishlist, bindingResult);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testUpdateWishlist() {
        wishlist.setQuantity(10);
        ResponseEntity<Void> response = wishlistController.updateWishlist("Bearer token", wishlist, bindingResult);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
}