package service;

import dao.ProductDAO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ProductDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WishlistService implements IWishlistService {

    private static HashMap<Integer, ProductDTO> listItemsInWishlist = new HashMap<>();

    @Override
    public HashMap<Integer, ProductDTO> createWishlist(ProductDTO item) {
        listItemsInWishlist = new HashMap<>();
        listItemsInWishlist.put(item.getId(), item);
        return listItemsInWishlist;
    }

    @Override
    public HashMap<Integer, ProductDTO> addItemToWishlist(ProductDTO item) {
        if (!checkItemExist(item)) {
            listItemsInWishlist.put(item.getId(), item);
        }
        return listItemsInWishlist;
    }

    @Override
    public boolean checkItemExist(ProductDTO product) {
        for (Integer id : listItemsInWishlist.keySet()) {
            if (product.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public HashMap<Integer, ProductDTO> removeItem(ProductDTO product) {
        if (listItemsInWishlist.containsKey(product.getId())) {
            listItemsInWishlist.remove(product.getId());
        }
        return listItemsInWishlist;
    }

    @Override
    public Cookie getCookieByName(HttpServletRequest request, String cookieName) {
        Cookie[] arrCookies = request.getCookies();
        if (arrCookies != null) {
            for (Cookie cookie : arrCookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    @Override
    public void saveWishlistToCookie(HttpServletRequest request, HttpServletResponse response, String strItemsInWishList) {
        String cookieName = "Wishlist";
        Cookie cookieCart = getCookieByName(request, cookieName);

        if (cookieCart != null) {
            cookieCart.setValue(strItemsInWishList);
        } else {
            cookieCart = new Cookie(cookieName, strItemsInWishList);
        }

        cookieCart.setMaxAge(60 * 60 * 24 * 30 * 3); // 3 months
        response.addCookie(cookieCart);
    }

    @Override
    public String convertToString() {
        List<ProductDTO> list = new ArrayList<>(listItemsInWishlist.values());
        String result = "";
        for (ProductDTO productDTO : list) {
            result += productDTO.getId() + ",";
        }
        return result;
    }

    @Override
    public List<ProductDTO> getWishlistFromCookie(Cookie cookieWishlist) {
        ProductDAO pDao = new ProductDAO();
        List<ProductDTO> listItemsCart = new ArrayList<>();
        String inputString = cookieWishlist.getValue();
        if (inputString.endsWith(",")) {
            inputString = inputString.substring(0, inputString.length() - 1);
        }

        if (inputString.length() > 0) {
            String[] elements = inputString.split(",");
            for (int i = 0; i < elements.length; i++) {
                ProductDTO product = pDao.getProductByID(Integer.parseInt(elements[i].trim()));
                listItemsCart.add(product);
            }
        }

        // Add to the internal wishlist
        for (ProductDTO productDTO : listItemsCart) {
            addItemToWishlist(productDTO);
        }

        return listItemsCart;
    }
}