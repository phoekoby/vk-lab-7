package org.example.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.example.dao.ProductDAO;
import org.example.pojo.ProductDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class ProductsServlet extends HttpServlet {
    private final ProductDAO productDAO;
    private final         ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

    @Inject
    public ProductsServlet(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<ProductDTO> productDTOS = productDAO.getAll();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        printWriter.print("<html><head><title>Product</title></head>");
        printWriter.print("<body>");
        productDTOS.forEach(
                productDTO -> {
                    try {
                        printWriter.println("<div>" + objectWriter.writeValueAsString(productDTO) + "</div>");
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        printWriter.print("</body>");
        printWriter.println("</html>");
        printWriter.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String json = IOUtils.toString(req.getReader());
        ProductDTO productDTO = objectMapper.readValue(json, ProductDTO.class);
        productDTO = productDAO.save(productDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("text/html");

        PrintWriter printWriter = resp.getWriter();
        printWriter.print("<html><head><title>Product</title></head>");
        printWriter.print("<body>" + objectWriter.writeValueAsString(productDTO) + "</body>");
        printWriter.println("</html>");
        printWriter.close();
    }
}
