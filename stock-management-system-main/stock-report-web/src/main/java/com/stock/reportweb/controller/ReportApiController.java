package com.stock.reportweb.controller;

import com.stock.reportweb.dao.ReportDAO;
import com.stock.reportweb.model.DashboardSummary;
import com.stock.reportweb.model.SaleRecordRow;
import com.stock.reportweb.model.SalesTrendPoint;
import com.stock.reportweb.model.TopProductPoint;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ReportApiController {

    private final ReportDAO reportDAO = new ReportDAO();

    private void ensureLoggedIn(HttpSession session) {
        if (session == null || session.getAttribute("loggedInUser") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please log in first.");
        }
    }

    @GetMapping("/api/summary")
    public DashboardSummary getSummary(HttpSession session) throws Exception {
        ensureLoggedIn(session);
        return reportDAO.getDashboardSummary();
    }

    @GetMapping("/api/sales-records")
    public List<SaleRecordRow> getSalesRecords(HttpSession session) throws Exception {
        ensureLoggedIn(session);
        return reportDAO.getAllSalesRecords();
    }

    @GetMapping("/api/sales-trend")
    public List<SalesTrendPoint> getSalesTrend(HttpSession session) throws Exception {
        ensureLoggedIn(session);
        return reportDAO.getSalesTrend();
    }

    @GetMapping("/api/top-products")
    public List<TopProductPoint> getTopProducts(HttpSession session) throws Exception {
        ensureLoggedIn(session);
        return reportDAO.getTopProducts();
    }
}
