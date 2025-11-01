package ppi.e_commerce.Config;package ppi.e_commerce.Config;



import org.springframework.beans.factory.annotation.Autowired;import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.ui.Model;import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ControllerAdvice;import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ModelAttribute;import org.springframework.web.bind.annotation.ModelAttribute;

import ppi.e_commerce.Model.User;import ppi.e_commerce.Model.User;

import ppi.e_commerce.Repository.UserRepository;import ppi.e_commerce.Repository.UserRepository;

import ppi.e_commerce.Service.CartService;import ppi.e_commerce.Service.CartService;

import ppi.e_commerce.Service.CategoryService;import ppi.e_commerce.Service.CategoryService;

import ppi.e_commerce.Service.BrandService;import ppi.e_commerce.Service.BrandService;



import java.util.Optional;import java.util.Optional;



/**/**

 * GlobalControllerAdvice * GlobalControllerAdvice

 * Proporciona datos globales a todas las vistas * Proporciona datos globales a todas las vistas

 */ */

@ControllerAdvice@ControllerAdvice

public class GlobalControllerAdvice {public class GlobalControllerAdvice {



    @Autowired    @Autowired

    private CartService cartService;    private CartService cartService;



    @Autowired    @Autowired

    private UserRepository userRepository;    private UserRepository userRepository;



    @Autowired    @Autowired

    private CategoryService categoryService;    private CategoryService categoryService;



    @Autowired    @Autowired

    private BrandService brandService;    private BrandService brandService;



    /**    /**

     * Agregar contador del carrito a todas las vistas     * Agregar contador del carrito a todas las vistas

     */     */

    @ModelAttribute    @ModelAttribute

    public void addCartAttributes(Model model) {    public void addCartAttributes(Model model) {

        try {        try {

            User currentUser = getCurrentUser();            User currentUser = getCurrentUser();

                        

            if (currentUser != null) {            if (currentUser != null) {

                int cartItemCount = cartService.getCartItemCount(currentUser);                int cartItemCount = cartService.getCartItemCount(currentUser);

                model.addAttribute("cartItemCount", cartItemCount);                model.addAttribute("cartItemCount", cartItemCount);

                model.addAttribute("currentUser", currentUser);                model.addAttribute("currentUser", currentUser);

            } else {            } else {

                model.addAttribute("cartItemCount", 0);                model.addAttribute("cartItemCount", 0);

            }            }

        } catch (Exception e) {        } catch (Exception e) {

            model.addAttribute("cartItemCount", 0);            model.addAttribute("cartItemCount", 0);

        }        }

    }    }



    /**    /**

     * Agregar categorías y marcas a todas las vistas (para el menú)     * Agregar categorías y marcas a todas las vistas (para el menú)

     */     */

    @ModelAttribute    @ModelAttribute

    public void addGlobalData(Model model) {    public void addGlobalData(Model model) {

        try {        try {

            model.addAttribute("categories", categoryService.findActiveCategories());            model.addAttribute("categories", categoryService.findActiveCategories());

            model.addAttribute("brands", brandService.findActiveBrands());            model.addAttribute("brands", brandService.findActiveBrands());

        } catch (Exception e) {        } catch (Exception e) {

            // Silenciar errores si no hay datos            // Silenciar errores si no hay datos

        }        }

    }    }



    /**    /**

     * Obtener usuario actual autenticado     * Obtener usuario actual autenticado

     */     */

    private User getCurrentUser() {    private User getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {

            return null;            return null;

        }        }



        String username = auth.getName();        String username = auth.getName();

        Optional<User> userOpt = userRepository.findByUsername(username);        Optional<User> userOpt = userRepository.findByUsername(username);

                

        return userOpt.orElse(null);        return userOpt.orElse(null);

    }    }

}}