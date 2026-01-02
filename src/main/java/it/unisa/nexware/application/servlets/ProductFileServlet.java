package it.unisa.nexware.application.servlets;

import it.unisa.nexware.application.beans.ProductBean;
import it.unisa.nexware.application.dto.SessionMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@WebServlet(urlPatterns = { "/myNexware/products/productFileServlet", "/catalogue/imgs/*" })
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,    // 2MB Limite per RAM
        maxFileSize = 1024 * 1024 * 50,         // 50MB per file
        maxRequestSize = 1024 * 1024 * 100      // 100MB per richiesta
)
public class ProductFileServlet extends HttpServlet {

    private static final String BASE_PATH = System.getProperty("user.home") + File.separator + "nexware_data" + File.separator;
    final String IMGS_PATH = BASE_PATH + "product-images" + File.separator;
    final String FILE_PATH = BASE_PATH + "product-files" + File.separator;
    final String FALLBACK_IMG = "no_image_fallback.png";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        if (Boolean.TRUE.equals(request.getAttribute("filesUploadForward")) || "/myNexware/products/productFileServlet".equals(path))
            handleProductUpdate(request, response);
        else if ("/catalogue/imgs/getFileList".equals(path))
            handleGetFileList(request, response);
        else
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path: " + path);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        if (path.equals("/catalogue/imgs/getFileList")) {
            handleGetFileList(request, response);

            return;
        }

        if (path.startsWith("/catalogue/imgs/"))
            handleImageDisplay(request, response);
        else
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    private void handleImageDisplay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/getFileList")) {
            serveFallback(response);

            return;
        }

        String[] parts = pathInfo.split("/");
        if (parts.length < 3) {
            serveFallback(response);

            return;
        }

        String productId = parts[1];
        String imgNumber = parts[2];

        File imgDir = new File(IMGS_PATH + productId);
        File[] matches = null;

        if (imgDir.exists() && imgDir.isDirectory())
            matches = imgDir.listFiles((_, name) -> name.startsWith(imgNumber + "."));

        if (matches != null && matches.length > 0) {
            File imageToServe = matches[0];
            String mime = getServletContext().getMimeType(imageToServe.getName());
            if (mime == null)
                mime = "image/jpeg";

            response.setContentType(mime);
            response.setContentLength((int) imageToServe.length());
            Files.copy(imageToServe.toPath(), response.getOutputStream());
        } else
            serveFallback(response);
    }

    private void handleProductUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ProductBean product = (ProductBean) request.getAttribute("product");
        if (product == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Product attribute missing");

            return;
        }

        String productId = String.valueOf(product.getId());
        File imgDir = new File(IMGS_PATH + productId);
        File fileDir = new File(FILE_PATH + productId);

        if (!imgDir.exists())
            imgDir.mkdirs();
        if (!fileDir.exists())
            fileDir.mkdirs();

        String[] keptImages = request.getParameterValues("keptImages");
        List<String> keptImgIndices = keptImages != null ? Arrays.asList(keptImages) : new ArrayList<>();

        File[] currentImages = imgDir.listFiles();
        if (currentImages != null)
            for (File img : currentImages) {
                String imgNum = String.valueOf(extractNumberFromFilename(img));
                if (!keptImgIndices.contains(imgNum)) {
                    img.delete();
                }
            }

        currentImages = imgDir.listFiles();
        int nextIndex = 1;

        if (currentImages != null && currentImages.length > 0) {
            Arrays.sort(currentImages, Comparator.comparingInt(this::extractNumberFromFilename));
            for (File img : currentImages) {
                String ext = getExtension(img.getName());
                File target = new File(imgDir, nextIndex + ext);
                if (!img.getName().equals(target.getName()))
                    img.renameTo(target);

                nextIndex++;
            }
        }

        for (Part part : request.getParts())
            if ("images".equals(part.getName()) && part.getSize() > 0 && part.getContentType().startsWith("image/")) {
                String fileName = part.getSubmittedFileName();
                String ext = getExtension(fileName);
                File fileToSave = new File(imgDir, nextIndex + ext);
                Files.copy(part.getInputStream(), fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
                nextIndex++;
            }

        String[] keptFiles = request.getParameterValues("keptFiles");
        List<String> keptFileList = keptFiles != null ? Arrays.asList(keptFiles) : new ArrayList<>();

        File[] currentFiles = fileDir.listFiles();
        if (currentFiles != null)
            for (File f : currentFiles)
                if (!keptFileList.contains(f.getName()))
                    f.delete();

        for (Part part : request.getParts())
            if ("files".equals(part.getName()) && part.getSize() > 0) {
                String originalName = part.getSubmittedFileName();
                if (originalName != null) originalName = new File(originalName).getName();
                File fileToSave = new File(fileDir, originalName);
                Files.copy(part.getInputStream(), fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

        request.getSession().setAttribute("queryProduct", new SessionMessage(product.getName()));
        response.sendRedirect(request.getContextPath() + "/myNexware/products/");
    }

    private void handleGetFileList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductBean product = (ProductBean) request.getAttribute("product");
        String forwardPath = (String) request.getAttribute("productForward");
        String filesToo = (String) request.getAttribute("filesToo");

        if (product == null || forwardPath == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String productId = String.valueOf(product.getId());
        File imgDir = new File(IMGS_PATH + productId);
        File fileDir = new File(FILE_PATH + productId);

        int imgCount = 0;
        if (imgDir.exists() && imgDir.isDirectory()) {
            File[] files = imgDir.listFiles();
            if (files != null)
                imgCount = files.length;
        }
        request.setAttribute("productImgsCount", imgCount);

        if ("true".equals(filesToo) && fileDir.exists()) {
            File[] files = fileDir.listFiles();
            if (files != null) {
                JSONArray jsonList = new JSONArray();

                for (File f : files)
                    jsonList.put(f.getName());

                request.setAttribute("filesList", jsonList.toString());
            }
        }

        request.getRequestDispatcher(forwardPath).forward(request, response);
    }

    private void serveFallback(HttpServletResponse response) throws IOException {
        File fallback = new File(IMGS_PATH + FALLBACK_IMG);
        if (fallback.exists()) {
            response.setContentType("image/png");
            Files.copy(fallback.toPath(), response.getOutputStream());
        } else
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private String getExtension(String filename) {
        int i = filename.lastIndexOf('.');
        return (i > 0) ? filename.substring(i) : "";
    }

    private int extractNumberFromFilename(File f) {
        String name = f.getName();
        int dotIndex = name.lastIndexOf('.');
        String numberPart = (dotIndex != -1) ? name.substring(0, dotIndex) : name;
        try {
            return Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}