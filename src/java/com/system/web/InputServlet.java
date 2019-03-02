package com.system.web;

import com.system.model.YearLevelInfo;
import com.system.model.DBConnectionManager;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import java.sql.*;
import javax.servlet.http.HttpSession;
/**
 *
 * @author CalvinGabriel
 */
public class InputServlet extends HttpServlet {

    Connection con;
    Statement stmt;
        
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
           
            String syearLvl = request.getParameter("yearLevel");
            int iyearLvl = Integer.parseInt(syearLvl);
            String scutofftimeF = request.getParameter("cutofftimeFrom");
            int icutofftimeF = Integer.parseInt(scutofftimeF);
            String scutofftimeT = request.getParameter("cutofftimeTo");
            int icutofftimeT = Integer.parseInt(scutofftimeT);
            String snumofSec = request.getParameter("numofSection");
            int inumofSec = Integer.parseInt(snumofSec);
            String sSem = request.getParameter("semester");
            int iSem = Integer.parseInt(sSem);
            String sSY = request.getParameter("sy");
            int iSYF = Integer.parseInt(sSY);
            String smethod = request.getParameter("method");
            String snumofCourse = request.getParameter("numofC");
            int inumofCourse = Integer.parseInt(snumofCourse);
            int iSYT = iSYF + 1;
                HttpSession ssn = request.getSession(false);
                if(ssn == null)
                {
                    ssn = request.getSession();
                }    
                YearLevelInfo yl = new YearLevelInfo(iyearLvl, icutofftimeF, icutofftimeT, inumofSec, iSem, iSYF, iSYT, inumofCourse);
                String yearID = yl.generateYearID(iyearLvl, iSem, iSYF, iSYT);
                ssn.setAttribute("yrID", yearID);
                DBConnectionManager dbcon = new DBConnectionManager();
                con = dbcon.getConnection();
                String query = "INSERT INTO YearLevel(Year_Lvl,Cutoff_time_from,Cutoff_time_to,No_of_Sections,Semester,SY_from,SY_to,No_of_Courses) VALUES(?,?,?,?,?,?,?,?);";
                //Dont forget the yearID
                PreparedStatement ps = null;               
                ps = con.prepareStatement(query);
               
                ps.setInt(1, yl.getYearLevel());
                ps.setInt(2, yl.getCutoffTimeF());
                ps.setInt(3, yl.getCutoffTimeT());
                ps.setInt(4, yl.getNumofSec());
                ps.setInt(5, yl.getSem());
                ps.setInt(6, yl.getSYF());
                ps.setInt(7, yl.getSYT());
                ps.setInt(8, yl.getNumofCourse());
                //ps.setString(9, yearID);

                int i = ps.executeUpdate();
                if(i!=0)
                {
                   System.out.println("Data inserted");
                }
                else
                {
                   System.out.println("Failed to insert data");
                }
                ps.close();
                if(smethod.equals("AddCurriculum"))
                {
                    ssn.setAttribute("numCourse", inumofCourse);

                    RequestDispatcher rd = request.getRequestDispatcher("AddCurriculum.jsp");
                    rd.forward(request, response);
                }
                else if(smethod.equals("RetrieveCurriculum"))
                {
                    RequestDispatcher rd = request.getRequestDispatcher("RetrieveCurriculum.jsp");
                    rd.forward(request, response);
                }                     
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
