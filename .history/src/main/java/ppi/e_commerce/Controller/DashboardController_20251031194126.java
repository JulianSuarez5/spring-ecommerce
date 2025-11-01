package ppi.e_commerce.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ppi.e_commerce.Service.OrderService;
import ppi.e_commerce.Service.ProductService;
import ppi.e_commerce.Service.CategoryService;
import ppi.e_commerce.Service.BrandService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("stats", getDashboardStats());
        model.addAttribute("recentOrders", orderService.getRecentOrders(10));
        model.addAttribute("topProducts", getTopProducts());
        return "Admin/dashboard";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("pageTitle", "Reportes");
        model.addAttribute("salesData", getSalesReportData());
        model.addAttribute("orderStatusData", getOrderStatusData());
        model.addAttribute("categoryData", getCategoryData());
        return "Admin/reports";
    }

    @GetMapping("/analytics")
    public String analytics(Model model) {
        model.addAttribute("pageTitle", "Analíticas");
        model.addAttribute("userGrowth", getUserGrowthData());
        model.addAttribute("salesTrend", getSalesTrendData());
        model.addAttribute("productPerformance", getProductPerformanceData());
        return "Admin/analytics";
    }

    // Métodos auxiliares internos
    private Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        Double totalSales = orderService.getTotalSales();
        stats.put("totalSales", totalSales != null ? totalSales : 0.0);

        Long totalOrders = orderService.countOrdersByUser(null);
        stats.put("totalOrders", totalOrders != null ? totalOrders : 0);

        Long totalProducts = productService.countProducts();
        stats.put("totalProducts", totalProducts != null ? totalProducts : 0);

        Long totalCategories = categoryService.countCategories();
        stats.put("totalCategories", totalCategories != null ? totalCategories : 0);

        Long totalBrands = brandService.countBrands();
        stats.put("totalBrands", totalBrands != null ? totalBrands : 0);

        return stats;
    }

    private Map<String, Object> getTopProducts() {
        return new HashMap<>();
    }

    private Map<String, Object> getSalesReportData() {
        return new HashMap<>();
    }

    private Map<String, Object> getOrderStatusData() {
        return new HashMap<>();
    }

    private Map<String, Object> getCategoryData() {
        return new HashMap<>();
    }

    private Map<String, Object> getUserGrowthData() {
        return new HashMap<>();
    }

    private Map<String, Object> getSalesTrendData() {
        return new HashMap<>();
    }

    private Map<String, Object> getProductPerformanceData() {
        return new HashMap<>();
    }
}
