import java.util.HashMap;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.sql.DataSource;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joget.apps.app.model.*;
import org.joget.apps.app.service.*;
import org.joget.apps.form.model.*;
import org.joget.apps.form.service.*;
import org.joget.commons.util.*;
import org.joget.directory.dao.UserDao;
import org.joget.directory.dao.RoleDao;
import org.joget.directory.model.*;
import org.joget.directory.model.service.DirectoryUtil;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.directory.model.service.UserSecurity;
import org.joget.workflow.model.WorkflowAssignment;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import org.joget.workflow.util.WorkflowUtil;
import org.joget.plugin.base.ApplicationPlugin;
import org.joget.plugin.base.Plugin;
import org.joget.plugin.base.PluginManager;
import org.joget.plugin.property.model.PropertyEditable;
import org.joget.commons.util.PluginThread;
import java.io.IOException;
import org.joget.commons.util.UuidGenerator;
import org.joget.commons.util.SecurityUtil;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.Form;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.joget.commons.util.LogUtil;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.sql.DataSource;
import org.joget.workflow.model.service.*;
\/\/
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.service.WorkflowManager;
import org.joget.commons.util.LogUtil;
   LogUtil.warn(\" --------------------> UPDATE DOSSIER ---------------------------------------------------> \",\"\");
WorkflowManager workflowManager = (WorkflowManager) AppUtil.getApplicationContext().getBean(\"workflowManager\");
Connection con = null;
try {
        \/\/ retrieve connection from the default datasource
        DataSource ds = (DataSource)AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        con = ds.getConnection();
        \/\/ execute SQL query
        String dateCom=\"\";
        String activityPiD=\"\";
        if(!con.isClosed()) {
                String idDossier=\"\";
                PreparedStatement stmtSelectFolders = con.prepareStatement(\"select c.c_activityprocessid,c_decision_id,c.id,com.c_datecomm_id from jwdb.app_fd_demande_carte c join jwdb.app_fd_dossier_commission d on(c.id=d.c_id_dossier and d.c_id_commission=?) join app_fd_commission com on (com.id=?);\");
                    stmtSelectFolders.setString(1,\"#variable.processID#\");
                    stmtSelectFolders.setString(2,\"#variable.processID#\");
                ResultSet rs1 = stmtSelectFolders.executeQuery();
                while (rs1.next()) {
                          activityPiD=rs1.getString(1);
                          idDossier=rs1.getString(3);
                        LogUtil.warn(\" --------------------> select executed + activity PID-----------> \",activityPiD);
                          LogUtil.warn(\" --------------------> dossier executed -----------> \",idDossier);
                        String decision=rs1.getString(2);
                        dateCom=rs1.getString(\"c_datecomm_id\");
                        LogUtil.warn(\" --------------------> decision -----------> \",decision);
                    LogUtil.warn(\" --------------------> updated process -----------> \",\"\");
                    PreparedStatement stmtUpdateCarte = con.prepareStatement(\"update jwdb.app_fd_demande_carte set c_statut=?,c_personneInforme='لا',c_dateInformationPersonne='',c_docAjour='لا' , c_date_comm=? , c_id_old_commission=? where id=?;\");
                    stmtUpdateCarte.setString(1,decision);
                    stmtUpdateCarte.setString(4,idDossier);
                    stmtUpdateCarte.setString(2,dateCom);
                    stmtUpdateCarte.setString(3,\"#variable.processID?sql#\");
                    stmtUpdateCarte.executeUpdate();
                     LogUtil.warn(\" --------------------> ex update -----------> \",\"\");
                    PreparedStatement stmtSelect = con.prepareStatement(\"SELECT * FROM jwdb.shkassignmentstable WHERE ActivityProcessId=?;\");
                    stmtSelect.setString(1,activityPiD);
                    ResultSet rs2 = stmtSelect.executeQuery();
                    int counter=0;
                    while (rs2.next()){
                            String activityProcessDefName = rs2.getString(\"ActivityProcessDefName\");
                            String activityProcessId = rs2.getString(\"ActivityProcessId\");
                            String activityId = rs2.getString(\"ActivityId\");
                            String username =  rs2.getString(\"ResourceId\");
                            LogUtil.info(\" -------------------->  activityProcessDefName-----------> \",activityProcessDefName);
                            LogUtil.info(\" -------------------->  activityProcessId-----------> \",activityProcessId);
                            LogUtil.info(\" -------------------->  activityId-----------> \",activityId);
                            LogUtil.info(\" -------------------->  username-----------> \",username);
                            workflowManager.activityVariable(activityId,\"decision_id\",decision);
                            LogUtil.warn(\" -------------------->  reassign-----------> \",\"\");
                            workflowManager.assignmentForceComplete(activityProcessDefName,activityProcessId,activityId,username);
                            LogUtil.warn(\" -------------------->  reassign complete-----------> \",\"\");
                    }
                }
                LogUtil.warn(\" --------------------> uodate dossier-----------> \",\"#variable.processID#\");
                PreparedStatement stmtDelete = con.prepareStatement(\"update jwdb.app_fd_dossier_commission set c_isSaved='false' where c_id_commission=? and id!='';\");
                stmtDelete.setString(1,\"#variable.processID?sql#\");
                stmtDelete.executeUpdate();
                LogUtil.warn(\" --------------------> ex update -----------> \",\"\");
        }
} catch(Exception e) {
        LogUtil.error(\"assignmentForceComplete \", e, \"error while executing method\");
} finally {
        try {
                if(con != null) {
                        con.close();
                }
        } catch(SQLException e) {\/* ignored *\/}
}

\/\/ WEB SERVICE 2
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import org.joget.commons.util.UuidGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.WorkflowProcessResult;
import org.joget.workflow.model.service.*;
import org.joget.directory.model.Department;
import java.util.Collection;
import org.joget.apps.app.service.AppUtil;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.commons.util.LogUtil;
import java.lang.StringBuilder;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.model.FormRow;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
LogUtil.info(\"------------PROCESS 1---Web SERVICE  2 -------------\",\"\");
public void hitApi(JSONObject jsonData , String lien ,String tableId){
        try {
            URL url = new URL(lien);
            connHTTP = (HttpURLConnection) url.openConnection();
            connHTTP.setRequestMethod(\"POST\");
            connHTTP.setRequestProperty(\"Content-Type\", \"application\/json\");
            connHTTP.setRequestProperty(\"Accept\", \"application\/json\");
            connHTTP.setRequestProperty(\"apikey\", \"#envVariable.ApiKey#\");
            connHTTP.setDoOutput(true);
            OutputStream os = connHTTP.getOutputStream();
            byte[] input = jsonData.toString().getBytes(\"utf-8\");
            os.write(input, 0, input.length);
            int status = connHTTP.getResponseCode();
            \/\/   LogUtil.info(\"----->  json data  \", jsonData.toString());
            LogUtil.info(\"response code \", String.valueOf(status));
            if (status >= 300) {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getErrorStream()));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                    }
                    reader.close();
            } else {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getInputStream(), StandardCharsets.UTF_8));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                                LogUtil.info(\"response ----------------------------------------> \", line);
                                JSONObject jsonRes = new JSONObject(line);
                                LogUtil.info(\"insert line**********************************************\", jsonRes.toString());
                                WorkflowManager wm = (WorkflowManager) pluginManager.getBean(\"workflowManager\");
                                wm.activityVariable(workflowAssignment.getActivityId(),\"statut_api\", jsonRes.getString(\"codeR\"));
                                String insertSql3 = \"UPDATE app_fd_personne SET c_prenom_id =?, c_nom_id=? ,c_identifiantsocial_id=?, c_sexe_id=? ,  c_chainePere=? , c_chaineMere=?  where id=?\";
                                if(jsonRes.getString(\"codeR\").equals(\"1\")){
                                    insertLine(jsonRes,tableId,insertSql3);
                                }
                    }
                    reader.close();
            }
    } catch (Exception e) {
            LogUtil.error(\"WS 1 hit api 2\", e, \"Error retrieving info\");
    } finally {
            connHTTP.disconnect();
    }
}
public void insertLine(JSONObject res,String tableId, String insertSql3){
        LogUtil.info(\"insert line ----------------------------------------> \", res.toString());
        LogUtil.info(\"insert line table ----------------------------------------> \", tableId);
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        Connection con3 = ds.getConnection();
        try {
                if (!con3.isClosed()) {
                        String idSocial=res.getString(\"idSocial\");
                        String nom=res.getString(\"nom\");
                        String prenom=res.getString(\"prenom\");
                        String sexe = ((res.getString(\"genre\")).equals(\"1\")) ? \"ذكر\" : \"أنثى\";
                        String chainePere=res.getString(\"chainePere\");
                        String chaineMere=res.getString(\"chaineMere\");
                        PreparedStatement stmtInsert3 = con3.prepareStatement(insertSql3);
                        stmtInsert3.setString(1,prenom);
                        stmtInsert3.setString(2,nom);
                        stmtInsert3.setString(3,idSocial);
                        stmtInsert3.setString(4,sexe);
                        stmtInsert3.setString(5,chainePere);
                        stmtInsert3.setString(6,chaineMere);
                        stmtInsert3.setString(7,tableId);
                        LogUtil.info(\"QUERY  ----------------------------------------> \", insertSql3);
                        stmtInsert3.executeUpdate();
                }
        } catch (Exception ex) {
                LogUtil.error(\"WS 2 insert line\", ex, \"Error storing using jdbc\");
        } finally {
                try {
                        if (con3 != null) {
                                con3.close();
                        }
                } catch (Exception ex) {
                        LogUtil.error(\"WS 2 insert line\", ex, \"Error closing the jdbc connection\");
                }
        }
}
Connection con = null;
DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
private String lien = \"#envVariable.Process1WebService2EndPoint#\";
LogUtil.info(\"------------PROCESS 1---Web SERVICE  2-------------LINK -------------\",lien);
private HttpURLConnection connHTTP;
private BufferedReader reader;
String line;
StringBuilder responseContent = new StringBuilder();
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
LogUtil.info(\"------------RecordID---------------->\", recordid);
String dateStr=\"#variable.dateNaiss#\";
SimpleDateFormat dateFormat = new SimpleDateFormat(\"yyyy-MM-dd\");
Date date = dateFormat.parse(dateStr);
Calendar calendar = Calendar.getInstance();
calendar.setTime(date);
int day = calendar.get(Calendar.DAY_OF_MONTH);
int month = calendar.get(Calendar.MONTH) + 1; \/\/ Month is 0-based
int year = calendar.get(Calendar.YEAR);
String strDay = String.valueOf(day);
String strMonth = String.valueOf(month);
String strYear = String.valueOf(year);
JSONObject jsonData = new JSONObject();
jsonData.put(\"jourNaiss\",strDay);
jsonData.put(\"moisNaiss\",strMonth);
jsonData.put(\"anneeNaiss\",strYear);
jsonData.put(\"prenom\",\"#variable.prenom#\");
jsonData.put(\"nom\",\"#variable.nom#\");
jsonData.put(\"prenomPere\",\"#variable.prenomPere#\");
jsonData.put(\"prenomGrandPere\",\"#variable.nomGrandPere#\");
jsonData.put(\"prenomMere\",\"#variable.prenomMere#\");
jsonData.put(\"nomMere\",\"#variable.nomMere#\");
String sexe = (\"#variable.sexe#\"==\"ذكر\") ? \"1\" : \"2\";
jsonData.put(\"genre\",sexe);
LogUtil.info(\"------------JSON IBJECT ---------------->\", jsonData.toString());
hitApi(jsonData,lien,recordid);

\/\/ WEB SERVICE 1
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import org.joget.commons.util.UuidGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.WorkflowProcessResult;
import org.joget.workflow.model.service.*;
import org.joget.directory.model.Department;
import java.util.Collection;
import org.joget.apps.app.service.AppUtil;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.commons.util.LogUtil;
import java.lang.StringBuilder;
import org.joget.workflow.model.service.*;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.model.FormRow;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
LogUtil.info(\"------------PROCESS 1---Web SERVICE  1 -------------\",\"\");
public void hitApi(JSONObject jsonData , String lien ,String tableId){
        try {
            URL url = new URL(lien);
            connHTTP = (HttpURLConnection) url.openConnection();
            connHTTP.setRequestMethod(\"POST\");
            connHTTP.setRequestProperty(\"Content-Type\", \"application\/json\");
            connHTTP.setRequestProperty(\"Accept\", \"application\/json\");
            connHTTP.setRequestProperty(\"apikey\", \"#envVariable.ApiKey#\");
            connHTTP.setDoOutput(true);
            OutputStream os = connHTTP.getOutputStream();
            byte[] input = jsonData.toString().getBytes(\"utf-8\");
            os.write(input, 0, input.length);
            int status = connHTTP.getResponseCode();
            LogUtil.info(\"----->  json data  \", jsonData.toString());
            LogUtil.info(\"response code \", String.valueOf(status));
            if (status >= 300) {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getErrorStream()));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                    }
                    reader.close();
            } else {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getInputStream(), StandardCharsets.UTF_8));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                                LogUtil.info(\"response ----------------------------------------> \", line);
                                JSONObject jsonRes = new JSONObject(line);
                                WorkflowManager wm = (WorkflowManager) pluginManager.getBean(\"workflowManager\");
                                wm.activityVariable(workflowAssignment.getActivityId(),\"statut_api\", jsonRes.getString(\"codeR\"));
                                LogUtil.info(\"insert line**********************************************\", jsonRes.toString());
                                String insertSql3 = \"UPDATE app_fd_personne SET c_prenom_id =?, c_nom_id=? ,c_identifiantsocial_id=?, c_sexe_id=? ,  c_chainePere=? , c_chaineMere=?  where id=?\";
                                if(jsonRes.getString(\"codeR\").equals(\"1\")){
                                        insertLine(jsonRes,tableId,insertSql3);
                                }
                    }
                    reader.close();
            }
    } catch (Exception e) {
            LogUtil.error(\"web service 1\", e, \"ERROR\");
    } finally {
            connHTTP.disconnect();
    }
}
public void insertLine(JSONObject res,String tableId, String insertSql3){
        LogUtil.info(\"insert line ----------------------------------------> \", res.toString());
        LogUtil.info(\"insert line table ----------------------------------------> \", tableId);
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        Connection con3 = ds.getConnection();
        \/\/ LogUtil.info(\"QUERY\", query);
        try {
                if (!con3.isClosed()) {
                        String idSocial=res.getString(\"idSocial\");
                        String nom=res.getString(\"nom\");
                        String prenom=res.getString(\"prenom\");
                        String sexe = ((res.getString(\"genre\")).equals(\"1\")) ? \"ذكر\" : \"أنثى\";
                        String chainePere=res.getString(\"chainePere\");
                        String chaineMere=res.getString(\"chaineMere\");
                        PreparedStatement stmtInsert3 = con3.prepareStatement(insertSql3);
                        stmtInsert3.setString(1,prenom);
                        stmtInsert3.setString(2,nom);
                        stmtInsert3.setString(3,idSocial);
                        stmtInsert3.setString(4,sexe);
                        stmtInsert3.setString(5,chainePere);
                        stmtInsert3.setString(6,chaineMere);
                        stmtInsert3.setString(7,tableId);
                        LogUtil.info(\"QUERY  ----------------------------------------> \", insertSql3);
                        stmtInsert3.executeUpdate();
                }
        } catch (Exception ex) {
                LogUtil.error(\"insert line WS1\", ex, \"Error storing using jdbc\");
        } finally {
                try {
                        if (con3 != null) {
                                con3.close();
                        }
                } catch (Exception ex) {
                        LogUtil.error(\"insert line WS1\", ex, \"Error closing the jdbc connection\");
                }
        }
}
Connection con = null;
DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
private String lien = \"#envVariable.Process1WebService1EndPoint#\";
LogUtil.info(\"------------PROCESS 1---Web SERVICE  1--------------LINK -------------\",lien);
private HttpURLConnection connHTTP;
private BufferedReader reader;
String line;
StringBuilder responseContent = new StringBuilder();
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
LogUtil.info(\"------------RecordID---------------->\", recordid);
String dateStr=\"#variable.dateNaiss#\";
SimpleDateFormat dateFormat = new SimpleDateFormat(\"yyyy-MM-dd\");
Date date = dateFormat.parse(dateStr);
Calendar calendar = Calendar.getInstance();
calendar.setTime(date);
int day = calendar.get(Calendar.DAY_OF_MONTH);
int month = calendar.get(Calendar.MONTH) + 1; \/\/ Month is 0-based
int year = calendar.get(Calendar.YEAR);
String strDay = String.valueOf(day);
String strMonth = String.valueOf(month);
String strYear = String.valueOf(year);
JSONObject jsonData = new JSONObject();
jsonData.put(\"cin\",\"#variable.cin#\");
jsonData.put(\"jourNaiss\",strDay);
jsonData.put(\"moisNaiss\",strMonth);
jsonData.put(\"anneeNaiss\",strYear);
LogUtil.info(\"------------JSON IBJECT ---------------->\", jsonData.toString());
hitApi(jsonData,lien,recordid);

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import org.joget.commons.util.UuidGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.WorkflowProcessResult;
import org.joget.workflow.model.service.*;
import org.joget.directory.model.Department;
import java.util.Collection;
import org.joget.apps.app.service.AppUtil;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.commons.util.LogUtil;
import java.lang.StringBuilder;
import org.joget.workflow.model.service.*;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.model.FormRow;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
\/\/Get record Id from process
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
Connection con = null;
\/\/initialize
try {
        \/\/ retrieve connection from the default datasource
        DataSource ds = (DataSource)AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        con = ds.getConnection();
            LogUtil.warn(\" --------------------> in process 1 id=-----------> \",recordid);
        if(!con.isClosed()) {
               \/\/ GET USER ROLES ---------------------------------
                    PreparedStatement stmt = con.prepareStatement(\"UPDATE app_fd_personne SET c_finInscription='1' where id=?;\");
                    stmt.setString(1,recordid);
                    stmt.executeUpdate();
                }
    } catch(Exception e) {
        LogUtil.error(\"Error while trying to set tag fin inscription process 1\", e, \"error\");
} finally {
        try {
                if(con != null) {
                        con.close();
                }
        } catch(SQLException e) {\/* ignored *\/}
}

\/\/ WEB SERVICE CRES
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import org.joget.commons.util.UuidGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.WorkflowProcessResult;
import org.joget.workflow.model.service.*;
import org.joget.directory.model.Department;
import java.util.Collection;
import org.joget.apps.app.service.AppUtil;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.commons.util.LogUtil;
import java.lang.StringBuilder;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.model.FormRow;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
LogUtil.info(\"------------PROCESS 1---Web SERVICE  CRS -------------\",\"\");
public void hitApi(JSONObject jsonData , String lien ,String tableId){
        try {
            URL url = new URL(lien);
            connHTTP = (HttpURLConnection) url.openConnection();
            connHTTP.setRequestMethod(\"POST\");
            connHTTP.setRequestProperty(\"Content-Type\", \"application\/json\");
            connHTTP.setRequestProperty(\"Accept\", \"application\/json\");
            connHTTP.setDoOutput(true);
            OutputStream os = connHTTP.getOutputStream();
            byte[] input = jsonData.toString().getBytes(\"utf-8\");
            os.write(input, 0, input.length);
            int status = connHTTP.getResponseCode();
            LogUtil.info(\"----->  json data  \", jsonData.toString());
            LogUtil.info(\"response code \", String.valueOf(status));
            if (status >= 300) {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getErrorStream()));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                    }
                    reader.close();
            } else {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getInputStream(), StandardCharsets.UTF_8));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                                LogUtil.warn(\"response ----------------------------------------> \", line);
                                JSONObject jsonRes = new JSONObject(line);
                                LogUtil.info(\"insert line**********************************************\", jsonRes.toString());
                                String insertSql3 = \"UPDATE app_fd_personne SET c_prenommere_id=? , c_nommere_id=? ,c_prenomgrandpere_id=?,c_prenompere_id=?,c_nompere_id=? where id=?\";
                                if(\"1\".equals(\"#variable.statut_api#\")){
                                        insertLine(jsonRes,tableId,insertSql3);
                                }else{
                                    LogUtil.info(\"code RETOUR != 1**********************************************\", \"#variable.statut_api#\");
                                }
                    }
                    reader.close();
            }
    } catch (Exception e) {
            LogUtil.error(\"web service cres\", e, \"Error\");
    } finally {
            connHTTP.disconnect();
    }
}
public void insertLine(JSONObject res,String tableId, String insertSql3){
        LogUtil.info(\"insert line ----------------------------------------> \", res.toString());
        LogUtil.info(\"insert line table ----------------------------------------> \", tableId);
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        Connection con3 = ds.getConnection();
        try {
                if (!con3.isClosed()) {
                        String Prenom_mere =res.getString(\"prenomMere\");
                        String Nom_mere=res.getString(\"nomMere\");
                        String prenom_gp=res.getString(\"prenomGP\");
                        String prenom_pere=res.getString(\"prenomPere\");
                        PreparedStatement stmtInsert3 = con3.prepareStatement(insertSql3);
                        stmtInsert3.setString(1,Prenom_mere);
                        stmtInsert3.setString(2,Nom_mere);
                        stmtInsert3.setString(3,prenom_gp);
                        stmtInsert3.setString(4,prenom_pere);
                        stmtInsert3.setString(5,NomPere);
                        stmtInsert3.setString(6,tableId);
                        LogUtil.info(\"QUERY  ----------------------------------------> \", insertSql3);
                        stmtInsert3.executeUpdate();
                }
        } catch (Exception ex) {
                LogUtil.error(\"cres insert to db\", ex, \"Error storing using jdbc\");
        } finally {
                try {
                        if (con3 != null) {
                                con3.close();
                        }
                } catch (Exception ex) {
                        LogUtil.error(\"cres insert to db\", ex, \"Error closing the jdbc connection\");
                }
        }
}
Connection con = null;
DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
private String lien = \"#envVariable.Process1WebServiceCRESSEndPoint#\";
LogUtil.info(\"------------PROCESS 1---Web SERVICE  2-------------LINK -------------\",lien);
private HttpURLConnection connHTTP;
private BufferedReader reader;
String line;
StringBuilder responseContent = new StringBuilder();
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
LogUtil.info(\"------------RecordID---------------->\", recordid);
JSONObject jsonData = new JSONObject();
String dateStr=\"#variable.dateNaiss#\";
SimpleDateFormat dateFormat = new SimpleDateFormat(\"yyyy-MM-dd\");
Date date = dateFormat.parse(dateStr);
Calendar calendar = Calendar.getInstance();
calendar.setTime(date);
int day = calendar.get(Calendar.DAY_OF_MONTH);
int month = calendar.get(Calendar.MONTH) + 1; \/\/ Month is 0-based
int year = calendar.get(Calendar.YEAR);
String strDay = String.valueOf(day);
String strMonth = String.valueOf(month);
String strYear = String.valueOf(year);
jsonData.put(\"jourNaiss\",strDay);
jsonData.put(\"moisNaiss\",strMonth);
jsonData.put(\"anneeNaiss\",strYear);
String sexe = (\"#variable.sexe#\"==\"ذكر\") ? \"1\" : \"2\";
jsonData.put(\"sexe\",sexe);
String NomPere=\"\";
String cin=\"\";
    DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
    Connection conSelect = ds.getConnection();
    try {
            if (!conSelect.isClosed()) {
                    String SelectQuery = \"select * from app_fd_personne where id=?\";
                    PreparedStatement stmtSelect = conSelect.prepareStatement(SelectQuery);
                    stmtSelect.setString(1,recordid);
                    LogUtil.info(\"QUERY  ----------------------------------------> \", SelectQuery);
                    ResultSet rs=stmtSelect.executeQuery();
                    while(rs.next()){
                            NomPere=rs.getString(\"c_nom_id\");
                            jsonData.put(\"nom\",rs.getString(\"c_nom_id\"));
                            jsonData.put(\"chainePere\",rs.getString(\"c_chainePere\"));
                            jsonData.put(\"chaineMere\",rs.getString(\"c_chaineMere\"));
                    }
            }
    } catch (Exception ex) {
            LogUtil.error(\"cres get from db\", ex, \"Error retrieving\");
    } finally {
            try {
                    if (conSelect != null) {
                            conSelect.close();
                    }
            } catch (Exception ex) {
                    LogUtil.error(\"cres get from db\", ex, \"Error closing the jdbc connection\");
            }
    }
LogUtil.info(\"------------JSON IBJECT ---------------->\", jsonData.toString());
hitApi(jsonData,lien,recordid);

{"tools":[{"className":"org.joget.apps.app.lib.BeanShellTool","properties":{"script":"import org.joget.workflow.util.WorkflowUtil;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
public String getEncodedMesage(String message){
        String input = message;
        try {
                String encoded = URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
                encoded = encoded.replace(\"+\", \"%20\");
                return encoded;
        } catch (UnsupportedEncodingException e) {
                return \"Error\";
                LogUtil.error(\"Error in ANNULER_UPDATE_RENOUVELLEMENT ices\",e,\"Cannot get encoded message\");
        }
}
HttpServletResponse response = WorkflowUtil.getHttpServletResponse();
    response.sendRedirect(\"\/jw\/web\/userview\/#envVariable.appId#\/#envVariable.userViewId#\/_\/#form.personne.pageUrl#?flash=\"+getEncodedMesage(\"#form.personne.flashParam#\"));
"}}],"runInMultiThread":"","comment":"

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import org.joget.commons.util.UuidGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.WorkflowProcessResult;
import org.joget.workflow.model.service.*;
import org.joget.directory.model.Department;
import java.util.Collection;
import org.joget.apps.app.service.AppUtil;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.commons.util.LogUtil;
import java.lang.StringBuilder;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.model.FormRow;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Calendar;
import java.sql.*;
\/\/--------date
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
\/\/-----------deate
Calendar calendar = Calendar.getInstance();
int currentYear = calendar.get(Calendar.YEAR);
int nextYear = currentYear + 1;
String currentYearString = String.valueOf(currentYear);
String nextYearString = String.valueOf(nextYear);
\/\/ Now you can use the 'currentYearString' and 'nextYearString' variables
LogUtil.info(\"-----> ************** in script generation list personne  ***********************\",\"\");
\/* Post JSON *\/
\/\/ id ahmed = 824e3a42-3ab2-4e57-8726-ada87668528b
\/\/ id doc = 3a51fba5-9f18-45a3-850d-06f2c3190efa
\/\/ Environnement developpemnt
private String lien = \"#envVariable.url_api_doc#\";
LogUtil.info(\"-----> ************** lien ***********************\",lien);
private HttpURLConnection connHTTP;
private BufferedReader reader;
String line;
StringBuilder responseContent = new StringBuilder();
\/\/Get record Id from process
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
LogUtil.info(\"------------RecordID---demande carte ------------->\", recordid);
LogUtil.info(\"------------------------------------------------------>\",\"#variable.id_personne#\");
      \/\/  String type_cnt = \"\";
String source_path = \"MAS-IDMEJ-template_recu.docx\";\/\/ ---------------------------------------------------------------------------------------------------------------------------------
    String output_path = \"#envVariable.path_app_formuploads#\/demande_carte\/\"+recordid;\/\/------------------------------------------------------------------------------
String output_file_name = \"\";
String output_file_name2 = \"\";
JSONObject jsonData = new JSONObject();
jsonData.put(\"source_path\", source_path);
    String gouvernorat =\"\";
     String secteur_ul=\"\";
    JSONObject data = new JSONObject();
try {
        Connection con = null;
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        con = ds.getConnection();
        LogUtil.info(\"---------------------------->1\", \"\");
    String selectQuery = \" select p.c_gouv_id, p.c_carte_id,p.c_identifiantsocial_id,p.c_datenaiss_id,p.c_adresse_id,p.c_premiernumtelephone_id,p.c_numcartid_id,p.c_secteur_id,p.c_prenom_id,p.c_nom_id,d.c_field40,d.c_field39,d.c_label_autre_doc_2,d.c_label_autre_doc_1,d.c_docmedical_id,d.c_docmedicavis_id,d.c_carnetdesoin_id,d.createdBy,d.dateCreated from app_fd_personne p join app_fd_demande_carte d on (p.id= d.c_id_personne) where d.c_id_personne =? ;\";
    PreparedStatement stmt = con.prepareStatement(selectQuery);
    stmt.setString(1, \"#variable.id_personne#\");
    ResultSet rs = stmt.executeQuery();
     \/\/ gouver
        ResultSet rs = stmt.executeQuery();
        LogUtil.info(\"------------selectQuery---------------->3\", selectQuery);
        if (rs.next()) {
                    LogUtil.info(\"--------------QUERY RESULT-------------->\", \"\");
                gouvernorat = rs.getString(\"c_gouv_id\");
                String num_identite = rs.getString(\"c_numcartid_id\");
                String  type_identite= rs.getString(\"c_carte_id\");
                String id_social = rs.getString(\"c_identifiantsocial_id\");
                String date_naissance = rs.getString(\"c_datenaiss_id\");
                String adresse_demandeur = rs.getString(\"c_adresse_id\");;
                String num_tel = rs.getString(\"c_premiernumtelephone_id\");
                 secteur_ul = rs.getString(\"c_secteur_id\");
                String prenom_demandeur = rs.getString(\"c_prenom_id\");
                String nom_demandeur = rs.getString(\"c_nom_id\");
                String charge_dossier = rs.getString(\"createdBy\");
        \/\/get date ---------------
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss\");
        DateTimeFormatter newDateFormat = DateTimeFormatter.ofPattern(\"dd\/MM\/yyyy\");
        LocalDateTime dateTime = LocalDateTime.parse(rs.getString(\"dateCreated\"), dtf);
        \/\/get date -----------
                String date_demande = dateTime.toLocalDate().format(newDateFormat);;
        LogUtil.info(\"--------------QUERY 1 TXT gouv id -------------->\", gouvernorat);
               \/\/ data.put(\"gouvernorat\", gouvernorat);
                data.put(\"num_identite\", num_identite);
                data.put(\"type_identite\", type_identite);
                data.put(\"id_social\", id_social);
                data.put(\"date_naissance\", date_naissance);
                data.put(\"adresse_demandeur\", adresse_demandeur);
                data.put(\"num_tel\", num_tel);
                data.put(\"prenom_demandeur\", prenom_demandeur);
                data.put(\"nom_demandeur\", nom_demandeur);
                data.put(\"charge_dossier\", charge_dossier);
                data.put(\"date_demande\", date_demande);
                \/\/ ----------------------------------
                output_file_name = num_identite+\"MAS-IDMEJ-template_recu.docx\";
                output_file_name2 = num_identite+\"MAS-IDMEJ-template_recu.pdf\";
                JSONArray DocsArray = new JSONArray();
                JSONObject Docs = new JSONObject();
                \/\/projet.put(\"date_dem\", c_date_creation);
                if(rs.getString(\"c_carnetdesoin_id\")!=\"\" && rs.getString(\"c_carnetdesoin_id\")!=null){
                          JSONObject Docs = new JSONObject();
                        Docs.put(\"name\", \"نسخة من بطاقة علاج مسندة من طرف مؤسسة عامة\");
                           DocsArray.put(Docs);
                }
                if(rs.getString(\"c_docmedicavis_id\")!=\"\" && rs.getString(\"c_docmedicavis_id\")!=null){
                          JSONObject Docs = new JSONObject();
                        Docs.put(\"name\",\"شهادة قيس البصر\");
                           DocsArray.put(Docs);
                }
                if(rs.getString(\"c_docmedical_id\")!=\"\" && rs.getString(\"c_docmedical_id\")!=null){
                          JSONObject Docs = new JSONObject();
                        Docs.put(\"name\",\"شهادة قيس السمع\");
                           DocsArray.put(Docs);
                }
                if(rs.getString(\"c_label_autre_doc_1\")!=\"\" && rs.getString(\"c_label_autre_doc_1\")!=null){
                          JSONObject Docs = new JSONObject();
                        Docs.put(\"name\",rs.getString(\"c_label_autre_doc_1\"));
                           DocsArray.put(Docs);
                }
                 if(rs.getString(\"c_label_autre_doc_2\")!=\"\" && rs.getString(\"c_label_autre_doc_2\")!=null){
                          JSONObject Docs = new JSONObject();
                        Docs.put(\"name\",rs.getString(\"c_label_autre_doc_2\"));
                           DocsArray.put(Docs);
                }
               \/\/ DocsArray.put(Docs);
                data.put(\"list\", DocsArray);
                \/\/-----------------------------------
            data.put(\"date_demande\", date_demande);
    }
     \/\/select c_lib_secteur from app_fd_secteur;
    LogUtil.info(\"--------------QUERY gouv-------------->\", \"\");
    String selectDocData = \"select c_lib_gouv from app_fd_gouvernorat where id=? ;\";
    LogUtil.info(\"--------------QUERY gouv TXT-------------->\", selectDocData);
    LogUtil.info(\"--------------QUERY gouv TXT gouv id -------------->\", gouvernorat);
    stmt = con.prepareStatement(selectDocData);
    stmt.setString(1, gouvernorat);
    rs = stmt.executeQuery();
    while (rs.next()) {
         data.put(\"gouvernorat\", rs.getString(\"c_lib_gouv\"));
    }
    String selectDocData2 = \"select c_lib_secteur from app_fd_secteur where id=? ;\";
    LogUtil.info(\"--------------QUERY gouv TXT-------------->\", selectDocData2);
    LogUtil.info(\"--------------QUERY gouv TXT gouv id -------------->\", secteur_ul);
    stmt = con.prepareStatement(selectDocData2);
    stmt.setString(1, secteur_ul);
    rs = stmt.executeQuery();
    while (rs.next()) {
         data.put(\"secteur_ul\", rs.getString(\"c_lib_secteur\"));
    }
    LogUtil.info(\"------------data------------->\", data.toString());
      jsonData.put(\"data\", data);
    jsonData.put(\"output_path\", output_path);
    jsonData.put(\"output_file_name\", output_file_name);
        LogUtil.info(\"---------------------------->6\", jsonData.toString());
    \/\/LogUtil.info(\"---------------------------->documentData\", documentData.toString());
    \/\/update file name in field
    String UpdateFile = \"update app_fd_demande_carte set c_demandeFileDoc=? where id=? ;\";
    stmtUpdateFileName = con.prepareStatement(UpdateFile);
    stmtUpdateFileName.setString(1, output_file_name2);
    stmtUpdateFileName.setString(2, recordid);
    stmtUpdateFileName.executeUpdate();
        rs.close();
        stmt.close();
        con.close();
} catch (SQLException e) {
        LogUtil.error(\"genDoc\", e, \"Error executing SQL query\");
} catch (Exception ex) {
        LogUtil.error(\"genDoc\", ex, \"Error in script Generation genDoc\");
}
try {
        URL url = new URL(lien);
        connHTTP = (HttpURLConnection) url.openConnection();
         LogUtil.info(\"---------------------------->7\", \"\");
        \/\/ Request setup
        connHTTP.setRequestMethod(\"POST\");
    connHTTP.setRequestProperty(\"Content-Type\", \"application\/json\");
    connHTTP.setRequestProperty(\"Accept\", \"application\/json\");
        connHTTP.setDoOutput(true);
        OutputStream os = connHTTP.getOutputStream();
        byte[] input = jsonData.toString().getBytes(\"utf-8\");
        os.write(input, 0, input.length);
        \/\/ The response from the server
        int status = connHTTP.getResponseCode();
          LogUtil.info(\"----->  json data  \", jsonData.toString());
        LogUtil.info(\"response code \", String.valueOf(status));
        if (status >= 300) {
                reader = new BufferedReader(new InputStreamReader(connHTTP.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                        responseContent.append(line);
                }
                reader.close();
        } else {
                reader = new BufferedReader(new InputStreamReader(connHTTP.getInputStream()));
                while ((line = reader.readLine()) != null) {
                        responseContent.append(line);
                }
                reader.close();
        }
} catch (Exception e) {
        LogUtil.error(\"genDoc\", e, \"genDoc\");
} finally {
        connHTTP.disconnect();
}

\/\/mailing
\/\/envoi d'email
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.joget.apps.app.service.*;
import org.joget.apps.app.model.*;
import org.joget.apps.form.model.*;
import org.joget.apps.form.service.*;
import java.sql.*;
import java.util.*;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.model.AppActivity;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormStoreBinder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;
import java.sql.*;
import org.joget.commons.util.UuidGenerator;
import org.joget.apps.form.service.FormUtil;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.joget.commons.util.StringUtil;
import org.joget.apps.form.model.Form;
import org.joget.apps.form.model.*;
import org.joget.apps.form.service.*;
import java.util.*;
import java.sql.Date;
import java.util.Collection;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import org.joget.workflow.model.WorkflowAssignment;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppPluginUtil;
import org.joget.plugin.base.ApplicationPlugin;
import org.joget.plugin.base.Plugin;
import org.joget.plugin.base.PluginManager;
import org.joget.plugin.property.model.PropertyEditable;
import org.joget.workflow.util.WorkflowUtil;
import org.joget.commons.util.PluginThread;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Store;
import java.util.ArrayList;
import java.util.List;
import org.joget.apps.app.service.AppService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joget.plugin.base.PluginManager;
import org.joget.commons.util.UuidGenerator;
import org.joget.commons.util.PluginThread;
import org.apache.commons.mail.HtmlEmail;
import org.joget.commons.util.StringUtil;
import java.util.Random;
import org.joget.commons.util.UuidGenerator;
import javax.servlet.http.HttpServletRequest;
import org.joget.apps.app.service.AppUtil;
import java.util.Set;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.SecurityUtil;
import org.joget.directory.dao.UserDao;
import org.joget.directory.dao.RoleDao;
import org.joget.directory.model.User;
import org.joget.directory.model.service.DirectoryUtil;
import org.joget.directory.model.service.UserSecurity;
import org.joget.commons.util.StringUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joget.commons.util.UuidGenerator;
import java.util.Map;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.plugin.base.ApplicationPlugin;
import org.joget.plugin.base.Plugin;
import org.joget.plugin.base.PluginManager;
import org.joget.plugin.property.model.PropertyEditable;
import org.joget.directory.model.service.ExtDirectoryManager;
import java.lang.Thread;
    \/\/ config mail
    \/\/Reuse Email Tool to send separated email to a list of users;
    Plugin plugin = pluginManager.getPlugin(\"org.joget.apps.app.lib.EmailTool\");
    \/\/Get default properties (SMTP setting) for email tool
    Map propertiesMap = AppPluginUtil.getDefaultProperties(plugin, null, appDef, null);
    propertiesMap.put(\"pluginManager\", pluginManager);
    propertiesMap.put(\"appDef\", appDef);
    propertiesMap.put(\"request\", request);
    ApplicationPlugin emailTool = (ApplicationPlugin) plugin;
     Collection mail = new ArrayList();
 String msg=\"\";
    DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
    Connection con = ds.getConnection();
    String dateCom=\"\";
    String nomPres=\"\";
            \/\/
    try{
            String query = \"select * from app_fd_commission where id=?\";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1,\"#variable.processID#\");
    \/\/app_fd_membre where c_id_commission=?
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                    dateCom=rs.getString(\"c_datecomm_id\");
                    nomPres=rs.getString(\"c_nompres_id\");
                    LogUtil.warn(\"---------------------------------> send to date\"+dateCom+\" pres: \",nomPres);
            }
            String query2 = \"select * from app_fd_membre where c_id_commission=?\";
            PreparedStatement stmt2 = con.prepareStatement(query2);
            stmt2.setString(1,\"#variable.processID#\");
            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                    mail.add(rs2.getString(\"c_mailMembre\"));
                    LogUtil.warn(\"---------------------------------> send to mail: \",(rs2.getString(\"c_mailMembre\")));
            }
            String query3 = \"select email from dir_user where id=?\";
            PreparedStatement stmt3 = con.prepareStatement(query3);
            stmt3.setString(1,nomPres);
            ResultSet rs3 = stmt3.executeQuery();
            while (rs3.next()) {
                    mail.add(rs3.getString(1));
                    LogUtil.warn(\"---------------------------------> send to mail: \",(rs3.getString(1)));
            }
        } catch(Exception e) {
                LogUtil.error(\"Sample app - Form 1\", e, \"Error loading user data in load binder\");
        } finally {
                \/\/always close the connection after used
                try {
                        if(con != null) {
                                con.close();
                        }
                } catch(SQLException e) {\/* ignored *\/}
        }
        msg = \"وبعد،\
\"+\"في إطار أعمال اللجنة الجهوية للأشخاص المعوقين بولاية تونس، وتبعا للأمر عدد 3086 لسنة 2005 المتعلق بإحداث اللجان الجهوية للأشخاص المعوقين وتحديد مقاييس الإعاقة وشروط إسناد بطاقة الإعاقة وعملا بالفصل عدد 2 الذي يحدد تركيبة أعضاء اللجنة الجهوية للأشخاص المعوقين، أتشرف بدعوتكم لتعيين ممثل لحضور اللجنة في اجتماعها الدوري يوم \"+dateCom +\" بمقر قسم النهوض الاجتماعي \
 والسلام\";
            LogUtil.warn(\"---------------------------------> send to mail for loop\",msg);
            for(String m : mail ){
                         propertiesMap.put(\"toSpecific\",m);
                         propertiesMap.put(\"subject\", \"حول تعيين ممثل لحضور اللجنة الجهوية للأشخاص المعوقين\");
                         propertiesMap.put(\"message\", msg);
                        \/\/set properties and execute the tool
                        ((PropertyEditable) emailTool).setProperties(propertiesMap);
                        emailTool.execute(propertiesMap);
                        LogUtil.info(\"---> send to \"+m+\" message: \",msg);
                          try {
                             Thread.sleep(10000);
                        } catch (InterruptedException e) {
                        }
            }

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import org.joget.commons.util.UuidGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.WorkflowProcessResult;
import org.joget.workflow.model.service.*;
import org.joget.directory.model.Department;
import java.util.Collection;
import org.joget.apps.app.service.AppUtil;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.commons.util.LogUtil;
import java.lang.StringBuilder;
import org.joget.workflow.model.service.*;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.model.FormRow;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
\/\/Get record Id from process
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
WorkflowManager wm = (WorkflowManager) pluginManager.getBean(\"workflowManager\");
wm.activityVariable(workflowAssignment.getActivityId(),\"processID\", recordid);

{"tools":[{"className":"org.joget.apps.app.lib.BeanShellTool","properties":{"script":"\/\/ WEB SERVICE cnam
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import org.joget.commons.util.UuidGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.WorkflowProcessResult;
import org.joget.workflow.model.service.*;
import org.joget.directory.model.Department;
import java.util.Collection;
import org.joget.apps.app.service.AppUtil;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.commons.util.LogUtil;
import java.lang.StringBuilder;
import org.joget.workflow.model.service.*;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.model.FormRow;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
LogUtil.warn(\"------------PROCESS 2 ---Web SERVICE  CNAM  -------------\",\"\");
public void hitApi(JSONObject jsonData ,String lien ,String tableId,String token){
        try {
            URL url = new URL(lien);
            connHTTP = (HttpURLConnection) url.openConnection();
            \/\/ Request setup
            connHTTP.setRequestMethod(\"GET\");
            connHTTP.setRequestProperty(\"Content-Type\", \"application\/json\");
            connHTTP.setRequestProperty(\"Accept\", \"application\/json\");
            connHTTP.setRequestProperty(\"Authorization\", \"Bearer \" + token);
            connHTTP.setDoOutput(true);
            \/\/ OutputStream os = connHTTP.getOutputStream();
            \/\/ byte[] input = jsonData.toString().getBytes(\"utf-8\");
            \/\/ os.write(input, 0, input.length);
            int status = connHTTP.getResponseCode();
            \/\/ LogUtil.info(\"----->  json data  \", jsonData.toString());
            LogUtil.info(\"response code \", String.valueOf(status));
            if (status >= 300) {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getErrorStream()));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                            LogUtil.warn(\"response ----------------------------------------> \", line);
                    }
                    reader.close();
            } else {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getInputStream(), StandardCharsets.UTF_8));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                                LogUtil.info(\"response ----------------------------------------> \", line);
                                JSONObject jsonRes = new JSONObject(line);
                                LogUtil.info(\"insert line**********************************************\", jsonRes.toString());
                                JSONArray itemsArray = jsonRes.getJSONArray(\"items\");
                                JSONObject item = itemsArray.getJSONObject(0);
                                String libCaisseValue = item.getString(\"lib_caisse\");
                                LogUtil.warn(\"insert line**********************************************\", libCaisseValue);
                                String insertSql3 = \"UPDATE app_fd_demande_carte SET c_couv_id=? where id=?\";
                                insertLine(jsonRes,tableId,insertSql3,libCaisseValue);
                    }
                    reader.close();
            }
    } catch (Exception e) {
            LogUtil.error(\"web service 1\", e, \"ERROR\");
    } finally {
            connHTTP.disconnect();
    }
}
public String hitApiToken2(String lien){
        String token=\"\";
        try {
                URL url = new URL(lien);
                connHTTP = (HttpURLConnection) url.openConnection();
                \/\/ Request setup
                connHTTP.setRequestMethod(\"POST\");
                connHTTP.setRequestProperty(\"Content-Type\", \"application\/x-www-form-urlencoded\");
                connHTTP.setRequestProperty(\"Accept\", \"application\/json\");
                connHTTP.setDoOutput(true);
                connHTTP.setDoInput(true);
                String username = \"#envVariable.loginCnam#\";
                String password = \"#envVariable.passCnam#\";
                \/\/ LogUtil.warn(\"username**********************************************\", username);
                \/\/ LogUtil.warn(\"password**********************************************\", password);
                String auth = username + \":\" + password;
                String authHeaderValue = \"Basic \" + Base64.getEncoder().encodeToString(auth.getBytes());
                connHTTP.setRequestProperty(\"Authorization\", authHeaderValue);
                String postData = \"grant_type=client_credentials\";
                try{
                        DataOutputStream wr = new DataOutputStream(connHTTP.getOutputStream());
                        wr.writeBytes(postData);
                        wr.flush();
                }catch(Exception x){
                        LogUtil.error(\"---------->\",x,\"error\");
                }
            int status = connHTTP.getResponseCode();
            LogUtil.info(\"response code \", String.valueOf(status));
            if (status >= 300) {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getErrorStream()));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                            return token;
                    }
                    reader.close();
            } else {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getInputStream(), StandardCharsets.UTF_8));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                                LogUtil.info(\"response ----------------------------------------> \", line);
                                JSONObject jsonRes = new JSONObject(line);
                                token=jsonRes.getString(\"access_token\");
                                return token;
                    }
                    reader.close();
            }
    } catch (Exception e) {
            return token;
            LogUtil.error(\"web service 1\", e, \"ERROR\");
    } finally {
            return token;
            connHTTP.disconnect();
    }
}
public void insertLine(JSONObject res,String tableId, String insertSql3,String value){
        LogUtil.info(\"insert line ----------------------------------------> \", res.toString());
        LogUtil.info(\"insert line table ----------------------------------------> \", tableId);
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        Connection con3 = ds.getConnection();
        try {
                if (!con3.isClosed()) {
                        PreparedStatement stmtInsert3 = con3.prepareStatement(insertSql3);
                        stmtInsert3.setString(1,value);
                        stmtInsert3.setString(2,tableId);
                        LogUtil.info(\"QUERY  ----------------------------------------> \", insertSql3);
                        stmtInsert3.executeUpdate();
                }
        } catch (Exception ex) {
                LogUtil.error(\"Dictionnaire signalement\", ex, \"Error storing using jdbc\");
        } finally {
                try {
                        if (con3 != null) {
                                con3.close();
                        }
                } catch (Exception ex) {
                        LogUtil.error(\"Dictionnaire signalement\", ex, \"Error closing the jdbc connection\");
                }
        }
}
public String getValue(){
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        Connection con3 = ds.getConnection();
        String id=\"\";
        try {
                if (!con3.isClosed()) {
                        String qrIdSoc=\"select c_identifiantsocial_id from app_fd_personne where id=?\";
                        PreparedStatement stmtSelect = con3.prepareStatement(qrIdSoc);
                        String id_personne=\"#variable.id_personne#\";
                        LogUtil.warn(\"id personne  ----------------------------------------> \", id_personne);
                        stmtSelect.setString(1,id_personne);
                        LogUtil.warn(\"QUERY  ----------------------------------------> \", stmtSelect.toString());
                        ResultSet rs = stmtSelect.executeQuery();
                        while (rs.next()){
                                LogUtil.warn(\"idsocial  ----------------------------------------> \", rs.getString(1));
                                id=rs.getString(1);
                                return id;
                        }
                }
        } catch (Exception ex) {
                LogUtil.error(\"web service cnam\", ex, \"Error storing using jdbc\");
                return id;
        } finally {
                try {
                        if (con3 != null) {
                                con3.close();
                                return id;
                        }
                } catch (Exception ex) {
                        LogUtil.error(\"web service cnam\", ex, \"Error closing the jdbc connection\");
                        return id;
                }
        }
}
Connection con = null;
DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
private String lienToken = \"#envVariable.TokenCNAM#\";
\/\/ private String lien = \"https:\/\/apigw.cni.tn\/reg\/get\/bc\/bycindn\/1.0.0\/getBCIdSocDdByCinDn\";
\/\/+++++++++++++++++++++++++++
\/\/ LogUtil.info(\"------------PROCESS 2 CNAM--------------LINK -------------\",lien);
\/\/ LogUtil.info(\"------------PROCESS 2 CNAM- TOKEN -------------LINK -------------\",lienToken);
private HttpURLConnection connHTTP;
private BufferedReader reader;
String line;
StringBuilder responseContent = new StringBuilder();
\/\/Get record Id from process
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
\/\/ LogUtil.info(\"------------RecordID---------------->\", recordid);
\/\/initiate object
String id_Social=getValue();
String token=hitApiToken2(lienToken);
LogUtil.warn(\"------------id_Social---------------->\", id_Social);
private String lien = \"#envVariable.Process2WebServiceCNAMEndPoint#\/\"+id_Social;
LogUtil.warn(\"------------lien---------------->\", lien);
LogUtil.warn(\"------------token---------------->\", token);
JSONObject jsonData = new JSONObject();
jsonData.put(\"idSocial\",id_Social);
hitApi(jsonData,lien,recordid,token);
 "}},{"className":"org.joget.apps.app.lib.BeanShellTool","properties":{"script":"\/\/ WEB SERVICE AMEN
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import org.joget.commons.util.UuidGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.WorkflowProcessResult;
import org.joget.workflow.model.service.*;
import org.joget.directory.model.Department;
import java.util.Collection;
import org.joget.apps.app.service.AppUtil;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.commons.util.LogUtil;
import java.lang.StringBuilder;
import org.joget.workflow.model.service.*;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.model.FormRow;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
LogUtil.info(\"------------PROCESS  2 --Web SERVICE  AMEN -------------\",\"\");
public String getValue(){
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        Connection con3 = ds.getConnection();
        String id=\"\";
        try {
                if (!con3.isClosed()) {
                        String qrIdSoc=\"select c_identifiantsocial_id from app_fd_personne where id=?\";
                        PreparedStatement stmtSelect = con3.prepareStatement(qrIdSoc);
                        String id_personne=\"#variable.id_personne#\";
                        LogUtil.warn(\"id personne  ----------------------------------------> \", id_personne);
                        stmtSelect.setString(1,id_personne);
                        LogUtil.warn(\"QUERY  ----------------------------------------> \", stmtSelect.toString());
                        ResultSet rs = stmtSelect.executeQuery();
                        while (rs.next()){
                                LogUtil.warn(\"idsocial  ----------------------------------------> \", rs.getString(1));
                                id=rs.getString(1);
                                return id;
                        }
                }
        } catch (Exception ex) {
                LogUtil.error(\"web service cnam\", ex, \"Error storing using jdbc\");
                return id;
        } finally {
                try {
                        if (con3 != null) {
                                con3.close();
                                return id;
                        }
                } catch (Exception ex) {
                        LogUtil.error(\"web service cnam\", ex, \"Error closing the jdbc connection\");
                        return id;
                }
        }
}
public void hitApi(JSONObject jsonData , String lien ,String tableId){
        try {
            URL url = new URL(lien);
            connHTTP = (HttpURLConnection) url.openConnection();
            \/\/ Request setup
            connHTTP.setRequestMethod(\"POST\");
            connHTTP.setRequestProperty(\"Content-Type\", \"application\/json\");
            connHTTP.setRequestProperty(\"Accept\", \"application\/json\");
            \/\/ connHTTP.setRequestProperty(\"apikey\", \"#envVariable.ApiKey#\");
              String username = \"#envVariable.loginAmen#\";
              String password = \"#envVariable.passAmen#\";
            \/\/   LogUtil.warn(\"username**********************************************\", username);
            \/\/   LogUtil.warn(\"password**********************************************\", password);
              String auth = username + \":\" + password;
              byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
              String authHeaderValue = \"Basic \" + new String(encodedAuth);
              connHTTP.setRequestProperty(\"Authorization\", authHeaderValue);
            connHTTP.setDoOutput(true);
            OutputStream os = connHTTP.getOutputStream();
            byte[] input = jsonData.toString().getBytes(\"utf-8\");
            os.write(input, 0, input.length);
            int status = connHTTP.getResponseCode();
            LogUtil.warn(\"response code \", String.valueOf(status));
            if (status >= 300) {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getErrorStream()));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                    }
                    reader.close();
            } else {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getInputStream(), StandardCharsets.UTF_8));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                                LogUtil.info(\"response ----------------------------------------> \", line);
                                String responseString=line.toString();
                                String cleanedString=responseString.replace(\"[\", \"\").replace(\"]\", \"\");
                                LogUtil.info(\"cleanedString ----------------------------------------> \", cleanedString);
                                JSONObject jsonRes = new JSONObject(cleanedString);
                                LogUtil.info(\"--------------------->\",jsonRes.toString());
                               if(jsonRes.getString(\"code\")==0){
                                    String insertSql3 = \"UPDATE app_fd_personne SET c_couv_id=?  where id=?\";
                                    insertLine(jsonRes,tableId,insertSql3);
                               }
                    }
                    reader.close();
            }
    } catch (Exception e) {
            LogUtil.error(\"web service Amen\", e, \"ERROR\");
    } finally {
            connHTTP.disconnect();
    }
}
public void insertLine(JSONObject res,String tableId, String insertSql3){
        LogUtil.info(\"insert line ----------------------------------------> \", res.toString());
        LogUtil.info(\"insert line table ----------------------------------------> \", tableId);
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        Connection con3 = ds.getConnection();
        try {
                if (!con3.isClosed()) {
                        PreparedStatement stmtInsert3 = con3.prepareStatement(insertSql3);
                        stmtInsert3.setString(1,res.getString(\"libTypeSoin\"));
                        stmtInsert3.setString(2,tableId);
                        LogUtil.info(\"QUERY  ----------------------------------------> \", insertSql3);
                        stmtInsert3.executeUpdate();
                }
        } catch (Exception ex) {
                LogUtil.error(\"AMEN\", ex, \"Error storing using jdbc\");
        } finally {
                try {
                        if (con3 != null) {
                                con3.close();
                        }
                } catch (Exception ex) {
                        LogUtil.error(\"AMEN\", ex, \"Error closing the jdbc connection\");
                }
        }
}
Connection con = null;
DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
private String lien = \"#envVariable.webServiceAmen#\";
\/\/ LogUtil.info(\"------------PROCESS  2 --Web SERVICE  AMEN --------------LINK -------------\",lien);
private HttpURLConnection connHTTP;
private BufferedReader reader;
String line;
StringBuilder responseContent = new StringBuilder();
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
LogUtil.info(\"------------RecordID---------------->\", recordid);
JSONObject jsonData = new JSONObject();
String qrIdSoc=\"select c_identifiantsocial_id from app_fd_personne where id=?\";
String id_Social=getValue();
jsonData.put(\"ident_uni\",id_Social);
LogUtil.info(\"------------JSON IBJECT ---------------->\", jsonData.toString());
hitApi(jsonData,lien,recordid);
 "}}],"runInMultiThread":"","comment":"

\/\/gen doc invitation proch comm
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.service.WorkflowManager;
import org.joget.commons.util.LogUtil;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Collection;
import org.joget.apps.app.service.AppUtil;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.commons.util.LogUtil;
import java.lang.StringBuilder;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.model.FormRow;
\/\/--------date
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
\/\/-----------deate
WorkflowManager workflowManager = (WorkflowManager) AppUtil.getApplicationContext().getBean(\"workflowManager\");
private String lien = \"#envVariable.url_api_doc#\";
LogUtil.info(\"-----> ************** lien ***********************\",lien);
private HttpURLConnection connHTTP;
private BufferedReader reader;
String line;
StringBuilder responseContent = new StringBuilder();
\/\/Get record Id from process
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
LogUtil.info(\"------------RecordID---Commission ------------->\", recordid);
      \/\/  String type_cnt = \"\";
 \/\/ function hitAPI
public void HitApi(String lien,JSONObject jsonData){
    try {
            URL url = new URL(lien);
            connHTTP = (HttpURLConnection) url.openConnection();
             LogUtil.info(\"---------------------------->7\", \"\");
            \/\/ Request setup
            connHTTP.setRequestMethod(\"POST\");
        connHTTP.setRequestProperty(\"Content-Type\", \"application\/json\");
        connHTTP.setRequestProperty(\"Accept\", \"application\/json\");
            connHTTP.setDoOutput(true);
            OutputStream os = connHTTP.getOutputStream();
            byte[] input = jsonData.toString().getBytes(\"utf-8\");
            os.write(input, 0, input.length);
            \/\/ The response from the server
            int status = connHTTP.getResponseCode();
              LogUtil.info(\"----->  json data  \", jsonData.toString());
            LogUtil.info(\"response code \", String.valueOf(status));
            if (status >= 300) {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getErrorStream()));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                    }
                    reader.close();
            } else {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                    }
                    reader.close();
            }
    } catch (Exception e) {
            LogUtil.error(\"CaseVioo\", e, \"Error in script Generation fiche cv  post CaseVioo Ouverture\");
    } finally {
            connHTTP.disconnect();
    }
}
\/\/function hit API
Connection con = null;
try {
        \/\/ retrieve connection from the default datasource
        DataSource ds = (DataSource)AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        con = ds.getConnection();
        \/\/ execute SQL query
        if(!con.isClosed()) {
                String idDossier=\"\";
                PreparedStatement stmtSelectFolders = con.prepareStatement(\"update app_fd_dossier_commission set c_isSaved='true' where c_id_commission=?;\");
                stmtSelectFolders.setString(1,\"#variable.processID#\");
                stmtSelectFolders.executeUpdate();
                \/\/ generate doc invitation
             String selectDocData2 = \"select distinct c.c_id_personne ,c.c_decision_id,c.c_decisionCommentaire,p.c_nom_id,p.c_prenom_id,p.c_numcartid_id,c.c_ComplimentDocument,c.id,c.c_infoOldCommission,commi.c_datecomm_id,c.id from app_fd_demande_carte c right join app_fd_dossier_commission dc on(c.id=dc.c_id_dossier) join app_fd_personne p on (p.id=c.c_id_personne) join app_fd_commission commi on (commi.id=c_id_commission)where c_id_commission=? ;\";
            LogUtil.info(\"--------------QUERY membre TXT-------------->\", selectDocData2);
            stmt2 = con.prepareStatement(selectDocData2);
            stmt2.setString(1, recordid);
            rs = stmt2.executeQuery();
            JSONArray DecisionArray = new JSONArray();
            while (rs.next()) {
                        if(rs.getString(\"c_decision_id\").equals(\"إستدعاء للحضور\")){
                            LogUtil.info(\"--------------INSIDE INV-------------->\", \"\");
                                String source_pathINV = \"#envVariable.pathSourceINV#\";\/\/ ---------------------------------------------------------------------------------------------------------------------------------
                                String output_pathINV = \"#envVariable.path_app_formuploads#\/demande_carte\/\"+rs.getString(\"id\");\/\/------------------------------------------------------------------------------
                                String output_file_nameINV = rs.getString(\"c_numcartid_id\")+\"_Invitation_Commission.docx\";
                                String output_file_nameINV2 = rs.getString(\"c_numcartid_id\")+\"_Invitation_Commission.pdf\";
                                JSONObject jsonDataINV = new JSONObject();
                                jsonDataINV.put(\"source_path\", source_pathINV);
                                jsonDataINV.put(\"output_path\", output_pathINV);
                                jsonDataINV.put(\"output_file_name\", output_file_nameINV);
                                JSONObject dataINVF = new JSONObject();
                                \/\/parse object old commission and affect to new object
                                JSONObject oldComm=new JSONObject(rs.getString(\"c_infoOldCommission\"));
                                LogUtil.info(\"--------------parsed old-------------->\", \"\");
                                dataINVF.put(\"num_com\",oldComm.getString(\"num_com\"));
                                dataINVF.put(\"dateCom\",oldComm.getString(\"dateCom\"));
                                \/\/parse object old commission and affect to new object
                                \/\/get date ---------------
                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm\");
                                    DateTimeFormatter newDateFormat = DateTimeFormatter.ofPattern(\"dd\/MM\/yyyy\");
                                    LocalDateTime dateTime = LocalDateTime.parse(rs.getString(\"c_datecomm_id\"), dtf);
                                    DateTimeFormatter newTimeFormat = DateTimeFormatter.ofPattern(\"HH:mm\");
                                    String extractedTime = dateTime.toLocalTime().format(newTimeFormat);
                                    \/\/get date -----------
                                    String date_reunion = dateTime.toLocalDate().format(newDateFormat);
                                    String heure_reunion=extractedTime.toString();
                                \/\/ put data to object
                                dataINVF.put(\"dateComNew\",date_reunion);
                                dataINVF.put(\"heureComNew\",heure_reunion);
                                dataINVF.put(\"nom\",rs.getString(\"c_prenom_id\")+\" \"+rs.getString(\"c_nom_id\"));
                                jsonDataINV.put(\"data\",dataINVF);
                                LogUtil.warn(\"--------------API INV-------------->\",\"\" );
                                HitApi(lien,jsonDataINV);
                                LogUtil.warn(\"--------------API INV-------------->\",\"\" );
                                String UpdateFileInv = \"update app_fd_demande_carte set c_filePVDecisionCommission=? where id=? ;\";
                                stmtUpdateFileInvName = con.prepareStatement(UpdateFileInv);
                                stmtUpdateFileInvName.setString(1, output_file_nameINV2);
                                stmtUpdateFileInvName.setString(2,rs.getString(\"id\"));
                                stmtUpdateFileInvName.executeUpdate();
                        \/\/generation doc inv
                }
            }
        }
} catch(Exception e) {
        LogUtil.error(\"assignmentForceComplete \", e, \"error get DAO\");
} finally {
        try {
                if(con != null) {
                        con.close();
                }
        } catch(SQLException e) {\/* ignored *\/}
}

\/\/doc pv commission idmaj
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import org.joget.commons.util.UuidGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.WorkflowProcessResult;
import org.joget.workflow.model.service.*;
import org.joget.directory.model.Department;
import java.util.Collection;
import org.joget.apps.app.service.AppUtil;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.commons.util.LogUtil;
import java.lang.StringBuilder;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.model.FormRow;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Calendar;
import java.sql.*;
\/\/--------date
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
\/\/-----------deate
Calendar calendar = Calendar.getInstance();
int currentYear = calendar.get(Calendar.YEAR);
int nextYear = currentYear + 1;
String currentYearString = String.valueOf(currentYear);
String nextYearString = String.valueOf(nextYear);
\/\/ Now you can use the 'currentYearString' and 'nextYearString' variables
LogUtil.info(\"-----> ************** in script generation PV commission ***********************\",\"\");
\/* Post JSON *\/
\/\/ id ahmed = 824e3a42-3ab2-4e57-8726-ada87668528b
\/\/ id doc = 3a51fba5-9f18-45a3-850d-06f2c3190efa
\/\/ Environnement developpemnt
private String lien = \"#envVariable.url_api_doc#\";
LogUtil.info(\"-----> ************** lien ***********************\",lien);
private HttpURLConnection connHTTP;
private BufferedReader reader;
String line;
StringBuilder responseContent = new StringBuilder();
\/\/Get record Id from process
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
LogUtil.info(\"------------RecordID---Commission ------------->\", recordid);
      \/\/  String type_cnt = \"\";
 \/\/ function hitAPI
public void HitApi(String lien,JSONObject jsonData){
    try {
            URL url = new URL(lien);
            connHTTP = (HttpURLConnection) url.openConnection();
             LogUtil.info(\"---------------------------->7\", \"\");
            \/\/ Request setup
            connHTTP.setRequestMethod(\"POST\");
        connHTTP.setRequestProperty(\"Content-Type\", \"application\/json\");
        connHTTP.setRequestProperty(\"Accept\", \"application\/json\");
            connHTTP.setDoOutput(true);
            OutputStream os = connHTTP.getOutputStream();
            byte[] input = jsonData.toString().getBytes(\"utf-8\");
            os.write(input, 0, input.length);
            \/\/ The response from the server
            int status = connHTTP.getResponseCode();
              LogUtil.info(\"----->  json data  \", jsonData.toString());
            LogUtil.info(\"response code \", String.valueOf(status));
            if (status >= 300) {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getErrorStream()));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                    }
                    reader.close();
            } else {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                    }
                    reader.close();
            }
    } catch (Exception e) {
            LogUtil.error(\"CaseVioo\", e, \"Error in script Generation fiche cv  post CaseVioo Ouverture\");
    } finally {
            connHTTP.disconnect();
    }
}
\/\/function hit API
String source_path = \"#envVariable.pathSourceCommission#\";\/\/ ---------------------------------------------------------------------------------------------------------------------------------
String output_path = \"#envVariable.path_app_formuploads#\";\/\/------------------------------------------------------------------------------
String output_file_name = \"\";
String output_file_name2 = \"\";
String commission_num=\"\";
String date_reunion=\"\";
String heure_reunion=\"\";
JSONObject jsonData = new JSONObject();
jsonData.put(\"source_path\", source_path);
JSONObject data = new JSONObject();
try {
        Connection con = null;
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        con = ds.getConnection();
        LogUtil.info(\"---------------------------->1\", \"\");
    String selectQuery = \" select c_numcomm,c_datecomm_id,c_nompres_id from app_fd_commission where  id=? ;\";
    PreparedStatement stmt = con.prepareStatement(selectQuery);
    stmt.setString(1, recordid);
    ResultSet rs = stmt.executeQuery();
     \/\/ gouver
        ResultSet rs = stmt.executeQuery();
        LogUtil.info(\"------------selectQuery---------------->3\", selectQuery);
        if (rs.next()) {
                    LogUtil.info(\"--------------QUERY RESULT-------------->\", \"\");
                commission_num  = rs.getString(\"c_numcomm\");
                String commission_president = rs.getString(\"c_nompres_id\");
                \/\/get date ---------------
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm\");
                DateTimeFormatter newDateFormat = DateTimeFormatter.ofPattern(\"dd\/MM\/yyyy\");
                LocalDateTime dateTime = LocalDateTime.parse(rs.getString(\"c_datecomm_id\"), dtf);
                DateTimeFormatter newTimeFormat = DateTimeFormatter.ofPattern(\"HH:mm\");
                String extractedTime = dateTime.toLocalTime().format(newTimeFormat);
                \/\/get date -----------
                date_reunion = dateTime.toLocalDate().format(newDateFormat);
                heure_reunion=extractedTime.toString();
        \/\/ put data to object
                data.put(\"commission_num\", commission_num);
                data.put(\"commission_president\", commission_president);
                data.put(\"date_reunion\", date_reunion);
                data.put(\"temp_reunion\", heure_reunion);
                \/\/ ----------------------------------
                output_file_name = commission_num +\"MAS-IDMEJ-template_PVcommission.docx\";
                output_file_name2 = commission_num +\"MAS-IDMEJ-template_PVcommission.pdf\";
    }
    LogUtil.info(\"--------------QUERY membre-------------->\", \"\");
    String selectDocData = \"select c_presence_id,c_nommembre_id,c_premembre_id,c_fonction_id from app_fd_membre where c_id_commission=? ;\";
    LogUtil.info(\"--------------QUERY membre TXT-------------->\", selectDocData);
    stmt = con.prepareStatement(selectDocData);
    stmt.setString(1, recordid);
    rs = stmt.executeQuery();
    JSONArray MembreArray = new JSONArray();
    while (rs.next()) {
                  JSONObject membre = new JSONObject();
                    membre.put(\"nom\",rs.getString(\"c_premembre_id\")+\" \"+rs.getString(\"c_nommembre_id\"));
                    membre.put(\"fonction\",rs.getString(\"c_presence_id\"));
                    membre.put(\"fn\",rs.getString(\"c_fonction_id\"));
                    MembreArray.put(membre);
            }
    data.put(\"list\", MembreArray);
    \/\/ query list
    LogUtil.info(\"--------------QUERY membre-------------->\", \"\");
    String selectDocData2 = \"select distinct c.c_id_personne ,c.c_decision_id,c.c_decisionCommentaire,p.c_nom_id,p.c_prenom_id,p.c_numcartid_id,c.c_ComplimentDocument,c.id,c.c_autreDoc from app_fd_demande_carte c right join app_fd_dossier_commission dc on(c.id=dc.c_id_dossier) join app_fd_personne p on (p.id=c.c_id_personne) where c_id_commission=? ;\";
    LogUtil.info(\"--------------QUERY membre TXT-------------->\", selectDocData2);
    stmt2 = con.prepareStatement(selectDocData2);
    stmt2.setString(1, recordid);
    rs = stmt2.executeQuery();
    JSONArray DecisionArray = new JSONArray();
    while (rs.next()) {
                  JSONObject decision = new JSONObject();
                    decision.put(\"nom\",rs.getString(\"c_prenom_id\")+\" \"+rs.getString(\"c_nom_id\"));
                    decision.put(\"cin\",rs.getString(\"c_numcartid_id\"));
                    decision.put(\"decision\",rs.getString(\"c_decision_id\"));
                    decision.put(\"comm\",rs.getString(\"c_decisionCommentaire\"));
                    DecisionArray.put(decision);
                    if(rs.getString(\"c_decision_id\").equals(\"رفض الطلب\")){
                            String source_pathRefus = \"#envVariable.pathSourceRefus#\";\/\/ ---------------------------------------------------------------------------------------------------------------------------------
                            String output_pathRefus = \"#envVariable.path_app_formuploads#\/demande_carte\/\"+rs.getString(\"id\");\/\/------------------------------------------------------------------------------
                            String output_file_nameRefus = rs.getString(\"c_numcartid_id\")+\"_DecisionCommissionPV.docx\";
                            String output_file_nameRefus2 = rs.getString(\"c_numcartid_id\")+\"_DecisionCommissionPV.PDF\";
                            JSONObject jsonDataRefus = new JSONObject();
                            jsonDataRefus.put(\"source_path\", source_pathRefus);
                            jsonDataRefus.put(\"output_path\", output_pathRefus);
                            jsonDataRefus.put(\"output_file_name\", output_file_nameRefus);
                            JSONObject dataRefus = new JSONObject();
                            dataRefus.put(\"num_com\",commission_num);
                            dataRefus.put(\"dateCom\",date_reunion);
                            dataRefus.put(\"nom\",rs.getString(\"c_prenom_id\")+\" \"+rs.getString(\"c_nom_id\"));
                            jsonDataRefus.put(\"data\",dataRefus);
                            LogUtil.warn(\"--------------API REFUS-------------->\",\"\" );
                            HitApi(lien,jsonDataRefus);
                            LogUtil.warn(\"--------------API REFUS-------------->\",\"\" );
                            String UpdateFileRefus = \"update app_fd_demande_carte set c_filePVDecisionCommission=? where id=? ;\";
                            stmtUpdateFileRefusName = con.prepareStatement(UpdateFileRefus);
                            stmtUpdateFileRefusName.setString(1, output_file_nameRefus2);
                            stmtUpdateFileRefusName.setString(2,rs.getString(\"id\"));
                            stmtUpdateFileRefusName.executeUpdate();
                    }else  if(rs.getString(\"c_decision_id\").equals(\"قبول الطلب\")){
                            String source_pathRefus = \"#envVariable.pathSourceAcc#\";\/\/ ---------------------------------------------------------------------------------------------------------------------------------
                            String output_pathRefus = \"#envVariable.path_app_formuploads#\/demande_carte\/\"+rs.getString(\"id\");\/\/------------------------------------------------------------------------------
                            String output_file_nameRefus = rs.getString(\"c_numcartid_id\")+\"_DecisionCommissionPV.docx\";
                            String output_file_nameRefus2 = rs.getString(\"c_numcartid_id\")+\"_DecisionCommissionPV.pdf\";
                            JSONObject jsonDataRefus = new JSONObject();
                            jsonDataRefus.put(\"source_path\", source_pathRefus);
                            jsonDataRefus.put(\"output_path\", output_pathRefus);
                            jsonDataRefus.put(\"output_file_name\", output_file_nameRefus);
                            JSONObject dataRefus = new JSONObject();
                            dataRefus.put(\"num_com\",commission_num);
                            dataRefus.put(\"dateCom\",date_reunion);
                            dataRefus.put(\"nom\",rs.getString(\"c_prenom_id\")+\" \"+rs.getString(\"c_nom_id\"));
                            jsonDataRefus.put(\"data\",dataRefus);
                            LogUtil.warn(\"--------------API REFUS-------------->\",\"\" );
                            HitApi(lien,jsonDataRefus);
                            LogUtil.warn(\"--------------API REFUS-------------->\",\"\" );
                            String UpdateFileRefus = \"update app_fd_demande_carte set c_filePVDecisionCommission=? where id=? ;\";
                            stmtUpdateFileRefusName = con.prepareStatement(UpdateFileRefus);
                            stmtUpdateFileRefusName.setString(1, output_file_nameRefus2);
                            stmtUpdateFileRefusName.setString(2,rs.getString(\"id\"));
                            stmtUpdateFileRefusName.executeUpdate();
                    }
                    else  if(rs.getString(\"c_decision_id\").equals(\"إستكمال وثائق\")){
                            String source_pathDoc = \"#envVariable.pathSourceDocument#\";\/\/ ---------------------------------------------------------------------------------------------------------------------------------
                            String output_pathDoc =  \"#envVariable.path_app_formuploads#\/demande_carte\/\"+rs.getString(\"id\");\/\/------------------------------------------------------------------------------
                            String output_file_nameDoc = rs.getString(\"c_numcartid_id\")+\"_DecisionCommissionPV.docx\";
                            String output_file_nameDoc2 = rs.getString(\"c_numcartid_id\")+\"_DecisionCommissionPV.pdf\";
                            JSONObject jsonDataDocs = new JSONObject();
                            jsonDataDocs.put(\"source_path\", source_pathDoc);
                            jsonDataDocs.put(\"output_path\", output_pathDoc);
                            jsonDataDocs.put(\"output_file_name\", output_file_nameDoc);
                            JSONObject dataDocs = new JSONObject();
                            dataDocs.put(\"num_com\",commission_num);
                            dataDocs.put(\"dateCom\",date_reunion);
                            dataDocs.put(\"nom\",rs.getString(\"c_prenom_id\")+\" \"+rs.getString(\"c_nom_id\"));
                            LogUtil.warn(\"--------------API COMP 1-------------->\",\"\" );
                            String[] rows = rs.getString(\"c_ComplimentDocument\").split(\";\");
                              LogUtil.warn(\"--------------API COMP 2-------------->\",rs.getString(\"c_ComplimentDocument\") );
                            JSONArray DocsArray = new JSONArray();
                            for (int i = 0; i < rows.length; i++) {
                                    if(rows[i]!=\"وثيقة أخرى\"){
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put(\"name\", rows[i]);
                                        DocsArray.put(jsonObject);
                                    }
                        }
                            LogUtil.warn(\"--------------API COMP 3-------------->\",\"\" );
                        if(rs.getString(\"c_autreDoc\")!=\"\" && rs.getString(\"c_autreDoc\")!=null){
                                     JSONObject jsonObject = new JSONObject();
                                    jsonObject.put(\"name\", rs.getString(\"c_autreDoc\"));
                                    DocsArray.put(jsonObject);
                        }
                            dataDocs.put(\"list\",DocsArray);
                        LogUtil.warn(\"--------------API COMP 4-------------->\", dataDocs.toString());
                        LogUtil.warn(\"--------------API COMP 5-------------->\", jsonDataDocs.toString());
                            jsonDataDocs.put(\"data\",dataDocs);
                            LogUtil.warn(\"--------------API Doc-------------->\",\"\" );
                            HitApi(lien,jsonDataDocs);
                            LogUtil.warn(\"--------------API Doc-------------->\",\"\" );
                              String UpdateFilEDoc = \"update app_fd_demande_carte set c_filePVDecisionCommission=? where id=? ;\";
                            stmtUpdateFilEDocName = con.prepareStatement(UpdateFilEDoc);
                            stmtUpdateFilEDocName.setString(1, output_file_nameDoc2);
                            stmtUpdateFilEDocName.setString(2, rs.getString(\"id\"));
                            stmtUpdateFilEDocName.executeUpdate();
                    }else  if(rs.getString(\"c_decision_id\").equals(\"إستدعاء للحضور\")){
                            LogUtil.warn(\"--------------INV-------------->\",\"\" );
                            JSONObject jsonDataDocs = new JSONObject();
                            jsonDataDocs.put(\"num_com\",commission_num);
                            jsonDataDocs.put(\"dateCom\",date_reunion);
                            String query=\"update app_fd_demande_carte set c_infoOldCommission=? where id=?\";
                            LogUtil.info(\"--------------QUERY update demande info old commission-------------->\", \"\");
                            stmtUp = con.prepareStatement(query);
                            stmtUp.setString(1, jsonDataDocs.toString());
                            stmtUp.setString(2, rs.getString(\"id\"));
                            stmtUp.executeUpdate();
                            LogUtil.info(\"--------------QUERY update demande info old commission DONE -------------->\", \"\");
                    }
            }
    data.put(\"listDemande\", DecisionArray);
    LogUtil.info(\"------------data------------->\", data.toString());
    jsonData.put(\"data\", data);
    jsonData.put(\"output_path\", output_path+\"\/commission\/\"+recordid);
    jsonData.put(\"output_file_name\", output_file_name);
    LogUtil.info(\"---------------------------->6\", jsonData.toString());
    \/\/UPDATE FILE COMMISSON
    \/\/ commissionFileDocRecordID---Commission
    String UpdateFile = \"update app_fd_commission set c_commissionFileDoc=? where id=? ;\";
    stmtUpdateFileName = con.prepareStatement(UpdateFile);
    stmtUpdateFileName.setString(1, output_file_name2);
    stmtUpdateFileName.setString(2, recordid);
    stmtUpdateFileName.executeUpdate();
    \/\/UPDATE FILE COMMISSON
    HitApi(lien,jsonData);
        rs.close();
        stmt.close();
        con.close();
} catch (SQLException e) {
        LogUtil.error(\"CaseVioo\", e, \"Error executing SQL query\");
} catch (Exception ex) {
        LogUtil.error(\"CaseVioo\", ex, \"Error in script Generation fiche cv post CaseVioo Ouverture\");
}

import org.joget.workflow.model.service.*;
import java.lang.Exception;
import org.joget.commons.util.LogUtil;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.joget.workflow.model.WorkflowAssignment;
import org.joget.workflow.model.service.*;
String id_personne = \"#requestParam.id_personne#\";
String type_process = \"#requestParam.type_process#\";
LogUtil.info(\"----> id_personne\",id_personne);
 \/\/Get record Id from process
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
WorkflowManager wm = (WorkflowManager) pluginManager.getBean(\"workflowManager\");
wm.activityVariable(workflowAssignment.getActivityId(),\"id_personne\", id_personne);
wm.activityVariable(workflowAssignment.getActivityId(),\"type_process\", type_process);
wm.activityVariable(workflowAssignment.getActivityId(),\"processID\", recordid);

{"tools":[{"className":"org.joget.apps.app.lib.BeanShellTool","properties":{"script":"import org.joget.workflow.util.WorkflowUtil;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
public String getEncodedMesage(String message){
        String input = message;
        try {
                String encoded = URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
                encoded = encoded.replace(\"+\", \"%20\");
                return encoded;
        } catch (UnsupportedEncodingException e) {
                return \"Error\";
                LogUtil.error(\"Error in ANNULER_UPDATE_RENOUVELLEMENT ices\",e,\"Cannot get encoded message\");
        }
}
HttpServletResponse response = WorkflowUtil.getHttpServletResponse();
    response.sendRedirect(\"\/jw\/web\/userview\/#envVariable.appId#\/#envVariable.userViewId#\/_\/#form.commission.pageUrl#?flash=\"+getEncodedMesage(\"#form.commission.flashParam#\"));
"}}],"runInMultiThread":"","comment":"

{"tools":[{"className":"org.joget.apps.app.lib.BeanShellTool","properties":{"script":"import org.joget.workflow.util.WorkflowUtil;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
public String getEncodedMesage(String message){
        String input = message;
        try {
                String encoded = URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
                encoded = encoded.replace(\"+\", \"%20\");
                return encoded;
        } catch (UnsupportedEncodingException e) {
                return \"Error\";
                LogUtil.error(\"Error in ANNULER_UPDATE_RENOUVELLEMENT ices\",e,\"Cannot get encoded message\");
        }
}
HttpServletResponse response = WorkflowUtil.getHttpServletResponse();
    response.sendRedirect(\"\/jw\/web\/userview\/#envVariable.appId#\/#envVariable.userViewId#\/_\/#form.demande_carte.pageUrl#?flash=\"+getEncodedMesage(\"#form.demande_carte.flashParam#\"));
"}}],"runInMultiThread":"","comment":"

import java.util.HashMap;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.sql.DataSource;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joget.apps.app.model.*;
import org.joget.apps.app.service.*;
import org.joget.apps.form.model.*;
import org.joget.apps.form.service.*;
import org.joget.commons.util.*;
import org.joget.directory.dao.UserDao;
import org.joget.directory.dao.RoleDao;
import org.joget.directory.model.*;
import org.joget.directory.model.service.DirectoryUtil;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.directory.model.service.UserSecurity;
import org.joget.workflow.model.WorkflowAssignment;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import org.joget.workflow.util.WorkflowUtil;
import org.joget.plugin.base.ApplicationPlugin;
import org.joget.plugin.base.Plugin;
import org.joget.plugin.base.PluginManager;
import org.joget.plugin.property.model.PropertyEditable;
import org.joget.commons.util.PluginThread;
import java.io.IOException;
import org.joget.commons.util.UuidGenerator;
import org.joget.commons.util.SecurityUtil;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.Form;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.joget.commons.util.LogUtil;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.sql.DataSource;
import org.joget.workflow.model.service.*;
\/\/
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.service.WorkflowManager;
import org.joget.commons.util.LogUtil;
   LogUtil.warn(\" --------------------> UPDATE DOSSIER ---------------------------------------------------> \",\"\");
WorkflowManager workflowManager = (WorkflowManager) AppUtil.getApplicationContext().getBean(\"workflowManager\");
Connection con = null;
try {
        \/\/ retrieve connection from the default datasource
        DataSource ds = (DataSource)AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        con = ds.getConnection();
        \/\/ execute SQL query
        String dateCom=\"\";
        String activityPiD=\"\";
        if(!con.isClosed()) {
                String idDossier=\"\";
                PreparedStatement stmtSelectFolders = con.prepareStatement(\"select c.c_activityprocessid,c_decision_id,c.id,com.c_datecomm_id from jwdb.app_fd_demande_carte c join jwdb.app_fd_dossier_commission d on(c.id=d.c_id_dossier and d.c_id_commission=?) join app_fd_commission com on (com.id=?);\");
                    stmtSelectFolders.setString(1,\"#variable.processID?sql#\");
                    stmtSelectFolders.setString(2,\"#variable.processID?sql#\");
                ResultSet rs1 = stmtSelectFolders.executeQuery();
                while (rs1.next()) {
                          activityPiD=rs1.getString(1);
                          idDossier=rs1.getString(3);
                        LogUtil.warn(\" --------------------> select executed + activity PID-----------> \",activityPiD);
                          LogUtil.warn(\" --------------------> dossier executed -----------> \",idDossier);
                        String decision=rs1.getString(2);
                        dateCom=rs1.getString(\"c_datecomm_id\");
                        LogUtil.warn(\" --------------------> decision -----------> \",decision);
                    LogUtil.warn(\" --------------------> updated process -----------> \",\"\");
                    PreparedStatement stmtUpdateCarte = con.prepareStatement(\"update jwdb.app_fd_demande_carte set c_statut=?,c_personneInforme='لا',c_dateInformationPersonne='',c_docAjour='لا' , c_date_comm=? where id=?;\");
                    stmtUpdateCarte.setString(1,decision);
                    stmtUpdateCarte.setString(3,idDossier);
                    stmtUpdateCarte.setString(2,dateCom);
                    stmtUpdateCarte.executeUpdate();
                     LogUtil.warn(\" --------------------> ex update -----------> \",\"\");
                    PreparedStatement stmtSelect = con.prepareStatement(\"SELECT * FROM jwdb.shkassignmentstable WHERE ActivityProcessId=?;\");
                    stmtSelect.setString(1,activityPiD);
                    ResultSet rs2 = stmtSelect.executeQuery();
                    int counter=0;
                    while (rs2.next()){
                            String activityProcessDefName = rs2.getString(\"ActivityProcessDefName\");
                            String activityProcessId = rs2.getString(\"ActivityProcessId\");
                            String activityId = rs2.getString(\"ActivityId\");
                            String username =  rs2.getString(\"ResourceId\");
                            LogUtil.info(\" -------------------->  activityProcessDefName-----------> \",activityProcessDefName);
                            LogUtil.info(\" -------------------->  activityProcessId-----------> \",activityProcessId);
                            LogUtil.info(\" -------------------->  activityId-----------> \",activityId);
                            LogUtil.info(\" -------------------->  username-----------> \",username);
                            workflowManager.activityVariable(activityId,\"decision_id\",decision);
                            LogUtil.warn(\" -------------------->  reassign-----------> \",\"\");
                            workflowManager.assignmentForceComplete(activityProcessDefName,activityProcessId,activityId,username);
                            LogUtil.warn(\" -------------------->  reassign complete-----------> \",\"\");
                    }
                }
                LogUtil.warn(\" --------------------> uodate dossier-----------> \",\"#variable.processID#\");
                PreparedStatement stmtDelete = con.prepareStatement(\"update jwdb.app_fd_dossier_commission set c_isSaved='false' where c_id_commission=? and id!='';\");
                stmtDelete.setString(1,\"#variable.processID?sql#\");
                stmtDelete.executeUpdate();
                LogUtil.warn(\" --------------------> ex update -----------> \",\"\");
        }
} catch(Exception e) {
        LogUtil.error(\"assignmentForceComplete \", e, \"error while executing method\");
} finally {
        try {
                if(con != null) {
                        con.close();
                }
        } catch(SQLException e) {\/* ignored *\/}
}

import java.util.Collection;
Collection assignees = new ArrayList();
assignees.add(\"#currentUser.username#\");
return assignees;

import java.util.Collection;
import org.joget.apps.app.service.AppUtil;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.directory.model.Department;
import org.joget.commons.util.LogUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.json.JSONObject;
import org.joget.workflow.model.service.*;
import java.lang.Exception;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.joget.workflow.model.WorkflowAssignment;
import org.joget.workflow.model.service.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.WorkflowProcessResult;
import org.joget.workflow.model.service.*;
import org.joget.directory.model.Department;
import java.util.Collection;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.commons.util.LogUtil;
import java.lang.StringBuilder;
import org.json.JSONObject;
\/\/Get record Id from process
\/\/ AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
\/\/ String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
Connection con = null;
Collection assignees = new ArrayList();
assignees.add(\"#currentUser.username#\");
\/\/ get current user locale
String currentUserLocale=\"#currentUser.locale#\";
JSONObject ob = new JSONObject(currentUserLocale);
String userGouv=ob.getString(\"gouv\");
String userDel=ob.getString(\"del\");
String userSec=ob.getString(\"sec\");
\/\/initialize
try {
        \/\/ retrieve connection from the default datasource
        DataSource ds = (DataSource)AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        con = ds.getConnection();
            \/\/ LogUtil.info(\" --------------------> in process tasjil ID-----------> \",recordid);
        if(!con.isClosed()) {
               \/\/ GET USER ROLES ---------------------------------
                    PreparedStatement stmtSelectFolders = con.prepareStatement(\"select id,locale from jwdb.dir_user where locale!=\"\";\");
                    ResultSet rs1 = stmtSelectFolders.executeQuery();
                    while (rs1.next()) {
                           JSONObject ob = new JSONObject(rs1.getString(\"locale\"));
                            String gouv=ob.getString(\"gouv\");
                            String del=ob.getString(\"del\");
                            String sec=ob.getString(\"sec\");
                            String role=ob.getString(\"list\");
                            if(del.equals(userDel) && role.contains(\"MAS-CHU\")){
                                    assignees.add(rs1.getString(\"id\"));
                            }
                        }
                }
            \/\/GET PERSONE demands ------------------------------------------------------
        \/\/------------------------------------------------------
    } catch(Exception e) {
        LogUtil.error(\"Serring users to process \", e, \"error\");
} finally {
        try {
                if(con != null) {
                        con.close();
                }
        } catch(SQLException e) {\/* ignored *\/}
}
return assignees;

\/\/ DDRASS
import java.util.Collection;
import org.joget.apps.app.service.AppUtil;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.directory.model.Department;
import org.joget.commons.util.LogUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.json.JSONObject;
import org.joget.workflow.model.service.*;
import java.lang.Exception;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.joget.workflow.model.WorkflowAssignment;
import org.joget.workflow.model.service.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.WorkflowProcessResult;
import org.joget.workflow.model.service.*;
import org.joget.directory.model.Department;
import java.util.Collection;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.commons.util.LogUtil;
import java.lang.StringBuilder;
import org.json.JSONObject;
\/\/Get record Id from process
String recordid =\"#variable.processID#\";
Connection con = null;
Collection assignees = new ArrayList();
\/\/ assignees.add(\"#currentUser.username#\");
\/\/ get current user locale
String currentUserLocale=\"#currentUser.locale#\";
JSONObject ob = new JSONObject(currentUserLocale);
String userGouv=ob.getString(\"gouv\");
String userDel=ob.getString(\"del\");
String userSec=ob.getString(\"sec\");
\/\/initialize
try {
        \/\/ retrieve connection from the default datasource
        DataSource ds = (DataSource)AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        con = ds.getConnection();
            \/\/ LogUtil.warn(\" --------------------> Adding participant to process-----------> \",\"\");
        if(!con.isClosed()) {
               \/\/ GET USER ROLES ---------------------------------
                 try{
                            PreparedStatement stmtSelectFolders = con.prepareStatement(\"select id,locale from jwdb.dir_user;\");
                        ResultSet rs1 = stmtSelectFolders.executeQuery();
                        while (rs1.next()) {
                                \/\/  LogUtil.warn(\" --------------------> selecting users for dir-----------> \",\"\");
                             if(rs1.getString(\"locale\")!=null && !rs1.getString(\"locale\").isEmpty()){
                                       JSONObject ob = new JSONObject(rs1.getString(\"locale\"));
                                    String gouv=ob.getString(\"gouv\");
                                    String del=ob.getString(\"del\");
                                    String sec=ob.getString(\"sec\");
                                    String role=ob.getString(\"list\");
                                    \/\/ LogUtil.warn(\" --------------------> parsed object -----------> \",\"\");
                                    if(gouv.equals(userGouv) && (role.contains(\"MAS-D-DRAS\") || role.contains(\"MAS-DPS-CHEF\")) ){
                                            \/\/ LogUtil.warn(\" --------------------> condition true -----------> \",\"\");
                                            assignees.add(rs1.getString(\"id\"));
                                            \/\/ LogUtil.info(\" --------------------> Added user-----------> \",rs1.getString(\"id\"));
                                    }
                             }
                            }
                 }catch(Exception x){
                         LogUtil.error(\" -------------------->errorr-----------> \",x,\"\");
                 }
                    try{
                                \/\/ GET USER ROLES ---------------------------------
                        PreparedStatement stms = con.prepareStatement(\"select c_nompres_id from jwdb.app_fd_commission where id=?;\");
                        stms.setString(1,recordid);
                        ResultSet rs2 = stms.executeQuery();
                        while (rs2.next()) {
                                    assignees.add(rs2.getString(\"c_nompres_id\"));
                                    \/\/ LogUtil.info(\" --------------------> Added user-----------> \",rs2.getString(\"c_nompres_id\"));
                            }
                    }catch(Exception x){
                             LogUtil.error(\" -------------------->errorr-----------> \",x,\"\");
                    }
                }
            \/\/GET PERSONE demands ------------------------------------------------------
        \/\/------------------------------------------------------
    } catch(Exception e) {
        LogUtil.warn(\"Setting users to process \",\"error\");
} finally {
        try {
                if(con != null) {
                        con.close();
                }
        } catch(SQLException e) {\/* ignored *\/}
}
return assignees;

