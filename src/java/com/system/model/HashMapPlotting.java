package com.system.model;

import java.util.*;
import java.sql.*;
/**
 *
 * @author CalvinGabriel
 */
public class HashMapPlotting {
    Connection con;
    PreparedStatement pdstmt;
    private HashMap<String, String[][]> hmap = new HashMap<String, String[][]>();
    private String[] roomAvlbl, roomToBeUsed;
    private String[][] room = new String[27][5];
    public HashMap HashMapPlotting(int nStudRoom, int nLecRooms, int nHybridRooms, int nCompRooms) throws ClassNotFoundException, SQLException
    {
        DBConnectionManager dbcon = new DBConnectionManager();
        con = dbcon.getConnection();
        String dbQuery = "SELECT Room_No FROM Room WHERE Status = A;";
        pdstmt = con.prepareStatement(dbQuery);
        ResultSet rs = pdstmt.executeQuery();
        
        int ctr = 0;
        while(rs.next())
        {
            roomAvlbl[ctr] = rs.getString("Room_No");
            ctr++;
        }
        System.arraycopy(roomAvlbl, 0, roomToBeUsed, 0, 4);
        for(int i=0;i<roomToBeUsed.length;i++)
        {
            String roomNo = roomToBeUsed[i];
            hmap.put(roomNo, room);
        }
        return (hmap);
    }
}
