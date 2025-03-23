package service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ProductDTO;
import java.util.HashMap;
import java.util.List;

public interface IWishlistService {
    HashMap<Integer, ProductDTO> createWishlist(ProductDTO item);
    HashMap<Integer, ProductDTO> addItemToWishlist(ProductDTO item);
    boolean checkItemExist(ProductDTO product);
    HashMap<Integer, ProductDTO> removeItem(ProductDTO product);
    Cookie getCookieByName(HttpServletRequest request, String cookieName);
    void saveWishlistToCookie(HttpServletRequest request, HttpServletResponse response, String strItemsInWishList);
    String convertToString();
    List<ProductDTO> getWishlistFromCookie(Cookie cookieWishlist);
}
