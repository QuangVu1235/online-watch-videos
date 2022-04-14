package com.poly.listener;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class Bai1_AppListener
 *
 */
@WebListener
public class AppListener implements HttpSessionListener, ServletContextListener {

	public AppListener() {
	}

	/**
	 * Chạy ngay sau khi một phiên làm việc được tạo 
	 * Tăng số đếm trong application scope lên một
	 */
	public void sessionCreated(HttpSessionEvent e) {
		HttpSession session = e.getSession();
		ServletContext application = session.getServletContext();
		Integer visitors = (Integer) application.getAttribute("visitors");
		application.setAttribute("visitors", visitors + 1);
	}

	/**
	 * Chạy ngay trước khi phiên làm việc hết hạn
	 */
	public void sessionDestroyed(HttpSessionEvent e) {
	}

	/**
	 * Chạy ngay trước khi ứng dụng web bị shutdown 
	 * Ghi số đếm trong application scope vào file
	 */
	public void contextDestroyed(ServletContextEvent e) {
		ServletContext application = e.getServletContext();
		Integer visitors = (Integer) application.getAttribute("visitors");

		try {
//			String path = application.getRealPath("/visitors.txt");
			Path path = Paths.get("D://counterServlet.txt");
			byte[] data = String.valueOf(visitors).getBytes();
//			Files.write(Paths.get(path), data, StandardOpenOption.CREATE);
			Files.write(path, data, StandardOpenOption.CREATE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Chạy ngay sau khi ứng dụng được khởi động
	 * Đọc số đếm trước đây từ file vào application scope
	 */
	public void contextInitialized(ServletContextEvent e) {
		ServletContext application = e.getServletContext();
		Integer visitors = 0;
		try {
//			String path = application.getRealPath("/visitors.txt");
			Path path = Paths.get("D://counterServlet.txt");

			// Tạo file nếu file chưa tồn tại
			File file = new File(path.toString());
			if (!file.exists()) {
				file.createNewFile();
			}

//			List<String> lines = Files.readAllLines(Paths.get(path));
			List<String> lines = Files.readAllLines(path);
			visitors = Integer.valueOf(lines.get(0));
		} catch (Exception ex) {
			visitors = 100000; // khởi đầu số khách
		}
		application.setAttribute("visitors", visitors);
	}

}
