package com.gen.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.gen.util.DbUtil;

public class TaskManagerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null && !action.isEmpty()) {
            switch (action) {
                case "add":
                    addTask(request, response);
                    break;
                case "update":
                    updateTask(request, response);
                    break;
                case "delete":
                    deleteTask(request, response);
                    break;
                case "fetch":
                    fetchTask(request, response);
                    break;
                default:
                    request.setAttribute("error", "Invalid action");
                    request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Action parameter not specified");
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
        }
    }

    private void addTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("user_id"));
        String project = request.getParameter("project");
        String dateStr = request.getParameter("date");
        String startTimeStr = request.getParameter("start_time");
        String endTimeStr = request.getParameter("end_time");
        BigDecimal duration = new BigDecimal(request.getParameter("duration"));
        String taskCategory = request.getParameter("task_category");
        String description = request.getParameter("description");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO tasks (user_id, project, date, start_time, end_time, duration, task_category, description) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            java.util.Date parsedDate = dateFormat.parse(dateStr);
            java.sql.Date date = new java.sql.Date(parsedDate.getTime());

            java.util.Date parsedStartTime = timeFormat.parse(startTimeStr);
            java.sql.Time startTime = new java.sql.Time(parsedStartTime.getTime());

            java.util.Date parsedEndTime = timeFormat.parse(endTimeStr);
            java.sql.Time endTime = new java.sql.Time(parsedEndTime.getTime());

            stmt.setInt(1, userId);
            stmt.setString(2, project);
            stmt.setDate(3, date);
            stmt.setTime(4, startTime);
            stmt.setTime(5, endTime);
            stmt.setBigDecimal(6, duration);
            stmt.setString(7, taskCategory);
            stmt.setString(8, description);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                request.setAttribute("message", "Task added successfully");
                request.getRequestDispatcher("success.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Failed to add task");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }

        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
            request.setAttribute("error", "An error occurred: " + ex.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void updateTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int taskId = Integer.parseInt(request.getParameter("task_id"));
        int userId = Integer.parseInt(request.getParameter("user_id"));
        String project = request.getParameter("project");
        String dateStr = request.getParameter("date");
        String startTimeStr = request.getParameter("start_time");
        String endTimeStr = request.getParameter("end_time");
        BigDecimal duration = new BigDecimal(request.getParameter("duration"));
        String taskCategory = request.getParameter("task_category");
        String description = request.getParameter("description");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE tasks SET user_id = ?, project = ?, date = ?, start_time = ?, end_time = ?, " +
                             "duration = ?, task_category = ?, description = ? WHERE task_id = ?")) {

            java.util.Date parsedDate = dateFormat.parse(dateStr);
            java.sql.Date date = new java.sql.Date(parsedDate.getTime());

            java.util.Date parsedStartTime = timeFormat.parse(startTimeStr);
            java.sql.Time startTime = new java.sql.Time(parsedStartTime.getTime());

            java.util.Date parsedEndTime = timeFormat.parse(endTimeStr);
            java.sql.Time endTime = new java.sql.Time(parsedEndTime.getTime());

            stmt.setInt(1, userId);
            stmt.setString(2, project);
            stmt.setDate(3, date);
            stmt.setTime(4, startTime);
            stmt.setTime(5, endTime);
            stmt.setBigDecimal(6, duration);
            stmt.setString(7, taskCategory);
            stmt.setString(8, description);
            stmt.setInt(9, taskId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                request.setAttribute("message", "Task updated successfully");
                request.getRequestDispatcher("success.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Failed to update task");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }

        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
            request.setAttribute("error", "An error occurred: " + ex.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void deleteTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int taskId = Integer.parseInt(request.getParameter("task_id"));

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM tasks WHERE task_id = ?")) {

            stmt.setInt(1, taskId);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                request.setAttribute("message", "Task deleted successfully");
                request.getRequestDispatcher("success.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Failed to delete task");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("error", "An error occurred: " + ex.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void fetchTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int taskId = Integer.parseInt(request.getParameter("task_id"));

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tasks WHERE task_id = ?")) {

            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String project = rs.getString("project");
                java.sql.Date date = rs.getDate("date");
                java.sql.Time startTime = rs.getTime("start_time");
                java.sql.Time endTime = rs.getTime("end_time");
                BigDecimal duration = rs.getBigDecimal("duration");
                String taskCategory = rs.getString("task_category");
                String description = rs.getString("description");

                request.setAttribute("task_id", taskId);
                request.setAttribute("user_id", userId);
                request.setAttribute("project", project);
                request.setAttribute("date", date);
                request.setAttribute("start_time", startTime);
                request.setAttribute("end_time", endTime);
                request.setAttribute("duration", duration);
                request.setAttribute("task_category", taskCategory);
                request.setAttribute("description", description);

                request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Task not found");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("error", "An error occurred: " + ex.getMessage());
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
        }
    }
}
