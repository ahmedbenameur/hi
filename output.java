public class Output {
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
