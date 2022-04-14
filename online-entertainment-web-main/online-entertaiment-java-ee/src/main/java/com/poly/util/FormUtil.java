package com.poly.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;

/**
 * Cung cấp các phương thức tiện ích làm việc với form nhập bao gồm cả upload
 * file và BeanUtils
 */
public class FormUtil {
	/**
	 * Kiểm tra sự tồn tại của tham số
	 * 
	 * @param name tên tham số
	 * @return true nếu tồn tại, ngược lại là false
	 */
	public static boolean exist(String name) {
		return RRSharer.request().getParameter(name) != null;
	}

	/**
	 * Đọc chuỗi từ tham số form
	 * 
	 * @param name   tên tham số form
	 * @param defval giá trị mặc định
	 * @return Giá trị tham số hoặc defval nếu tham số không tồn tại
	 */
	public static String getString(String name, String defval) {
		String value = RRSharer.request().getParameter(name);
		return value == null ? defval : value;
	}

	/**
	 * Đọc số nguyên từ tham số form
	 * 
	 * @param name   tên tham số form
	 * @param defval giá trị mặc định
	 * @return Giá trị tham số hoặc defval nếu tham số không tồn tại
	 */
	public static int getInt(String name, int defval) {
		String value = getString(name, String.valueOf(defval));
		return Integer.parseInt(value);
	}

	/**
	 * Đọc số thực từ tham số form
	 * 
	 * @param name   tên tham số form
	 * @param defval giá trị mặc định
	 * @return Giá trị tham số hoặc defval nếu tham số không tồn tại
	 */
	public static double getDouble(String name, double defval) {
		String value = getString(name, String.valueOf(defval));
		return Double.parseDouble(value);
	}

	/**
	 * Đọc giá trị boolean từ tham số form
	 * 
	 * @param name   tên tham số form
	 * @param defval giá trị mặc định
	 * @return Giá trị tham số hoặc defval nếu tham số không tồn tại
	 */
	public static boolean getBoolean(String name, boolean defval) {
		String value = getString(name, String.valueOf(defval));
		return Boolean.parseBoolean(value);
	}

	/**
	 * Đọc thời gian từ tham số form
	 * 
	 * @param name   tên tham số form
	 * @param defval giá trị mặc định
	 * @return Giá trị tham số hoặc defval nếu tham số không tồn tại
	 */
	public static Date getDate(String name, Date defval, String patternDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(patternDate);
		String date = getString(name, sdf.format(defval));
		try {
			return sdf.parse(date);
		} catch (Exception e) {
			return defval;
		}
	}

	/**
	 * Lưu file upload vào thư mục với tên duy nhất
	 * 
	 * @param name   tên tham số form
	 * @param folder thư mục chứa file
	 * @return file kết quả hoặc null nếu không upload
	 */
	public static File save(String name, String folder) {
		HttpSession session = RRSharer.request().getSession();
		ServletContext XHttp = session.getServletContext();
		File dir = new File(XHttp.getRealPath(folder));
		if (!dir.exists()) {
			dir.mkdirs();
		}
		try {
			Part part = RRSharer.request().getPart(name);
			if (part != null && part.getSize() > 0) {
				String fn = System.currentTimeMillis() + part.getSubmittedFileName();
				String filename = Integer.toHexString(fn.hashCode()) + fn.substring(fn.lastIndexOf("."));
				File file = new File(dir, filename);
				part.write(file.getAbsolutePath());
				return file;
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * Lấy tất cả file upload lưu vào thư mục
	 * @param folder thư mục chứa file
	 * @return danh sách các file kết quả
	 */
	public static List<File> saveFiles(String folder)
			throws IOException, ServletException {
		List<File> listFiles = null;
		
		HttpSession session = RRSharer.request().getSession();
		ServletContext XHttp = session.getServletContext();

		String uploadPath = XHttp.getRealPath("") + File.separator + folder;
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists())
			uploadDir.mkdir();

		try {
			listFiles = new ArrayList<>();
			Collection<Part> parts = RRSharer.request().getParts();

			for (Part part : parts) {
				String fileName = part.getSubmittedFileName();
				File file = new File(uploadPath + File.separator + fileName);
				if (fileName.isBlank() || fileName == null)
					continue;
				// Lưu tệp vào thư mục upload
				part.write(file.getAbsolutePath());

				listFiles.add(file);
			}
			
			return listFiles;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * Xoá tất cả file trong thư mục upload
	 * @param folder thư mục chứa file
	 */
	public static void deleteFiles(String folder) {
		HttpSession session = RRSharer.request().getSession();
		ServletContext XHttp = session.getServletContext();
		
		File dir = new File(XHttp.getRealPath(folder));
		
		FormUtil.deleteDirectory(dir);
	}
	public static boolean deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		return directoryToBeDeleted.delete();
	}
	/**
	 * Đọc các giá trị tham số form vào các thuộc tính cùng tên của bean
	 * 
	 * @return Bean chứa dữ liệu form
	 */
	public static <T> T getBean(Class<T> clazz, String patternDate) {
		DateTimeConverter dtc = new DateConverter(new Date());
		dtc.setPattern(patternDate);
		ConvertUtils.register(dtc, Date.class);
		try {
			T bean = clazz.getDeclaredConstructor().newInstance();
			BeanUtils.populate(bean, RRSharer.request().getParameterMap());
			return bean;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
