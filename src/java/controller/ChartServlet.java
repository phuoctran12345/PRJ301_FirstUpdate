/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.OrderDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ChartServlet", urlPatterns = {"/ChartServlet"})
public class ChartServlet extends HttpServlet {
    private static final String CHART_PAGE = "view/jsp/admin/admin_chart.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OrderDAO oDao = new OrderDAO();
        try {
            // Lấy doanh thu của các năm
            int[] years = {2018, 2019, 2020, 2021, 2022, 2023, 2024};
            double[] totalMoneyByYear = new double[years.length];
            for (int i = 0; i < years.length; i++) {
                totalMoneyByYear[i] = oDao.getTotalMoneyByYear(years[i]);
                request.setAttribute("YEAR" + (years[i] - 2017), totalMoneyByYear[i]);
            }

            // Lấy doanh thu của các tháng trong năm
            double[] totalMoneyByMonth = new double[12];
            for (int month = 1; month <= 12; month++) {
                totalMoneyByMonth[month - 1] = oDao.getTotalMoneyByMonth(month);
                request.setAttribute("MONTH" + month, totalMoneyByMonth[month - 1]);
            }

            // Thêm thông tin vào request để trang JSP có thể sử dụng
            request.setAttribute("CURRENTSERVLET", "Chart");

        } catch (Exception ex) {
            log("ChartServlet error:" + ex.getMessage());
            request.setAttribute("ERROR_MESSAGE", "Có lỗi xảy ra khi lấy dữ liệu thống kê.");
        } finally {
            request.getRequestDispatcher(CHART_PAGE).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet for generating statistics charts of total money by year and month.";
    }
}
