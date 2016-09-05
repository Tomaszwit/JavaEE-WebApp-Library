package zad1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ControllerServ extends HttpServlet{
	
	private ServletContext context;
	private Command command;			//obiekt klasy dzialania
	private String presentationServ;	//nazwa serwlet prezentacji
	private String getParamsServ;		//nazwa serwletu pobierania parametrow

	public void init()
	{
		context = getServletContext();
		
		presentationServ = context.getInitParameter("presentationServ");
		getParamsServ = context.getInitParameter("getParamsServ");
		String commandClassName = context.getInitParameter("commandClassName");
		
		String dbName = context.getInitParameter("dbName");

		
		//załadowanie klasy command i utworzenie jej egzemplarza
		//ktory bedzie wykonywał pracę
		
		try{
			Class commandClass = Class.forName(commandClassName);
			command = (Command) commandClass.newInstance();
			// ustalamy, na jakiej bazie ma działać Command i inicjujemy obiekt
		    command.setParameter("dbName", dbName);
		    command.init();
		    
		}catch(Exception exc){
			throw new NoCommandException("Nie mogę stworzyć obiektu klasy " + commandClassName);
		}
	}
	
	//OBSŁUGA ZLECEŃ
	public void serviceRequest(HttpServletRequest req,
							HttpServletResponse resp) throws ServletException, IOException
	{
		resp.setContentType("text/html");
		
		//wywolanie serwletu pobierania parametrow
		RequestDispatcher disp = context.getRequestDispatcher(getParamsServ);
		disp.include(req, resp);
		
		
		//pobranie biezacej sesji
		//i z jej atrybutow wartosci parametrow
		// ustalonych przez servlet pobierania parametrow
		//rozne informacje o aplikacji (np. nazwy parametrow)
		// są wygodnie dostępne poprzez własną klasę BundleInfo
		
		HttpSession ses = req.getSession();
		
		String [] pnames = BundleInfo.getCommandParamNames();
		for ( int i = 0; i < pnames.length; i++)
		{
			String pval = (String) ses.getAttribute("param_" + pnames[i]);
			
			if(pval == null) return;
			
			//ustalenie parametru dla command
			command.setParameter(pnames[i], pval);
		}
		
		//wykonanie dzialan przez command i pobranie wynikow
		//poniewaz do serwletu moze naraz odwoływać się wielu użytkowników (wątków) potrzebna jest synchronizacjia
		//użyjemy rygli na sekcji krytycznej czyli całym wowołaniu command
		
		Lock mainLock = new ReentrantLock();
		mainLock.lock();
		
		//wokonanie
		command.execute();
		
		//pobranie wyników
		List results = (List) command.getResults();
		
		//Pobranie i zapamiętanie kodu wyniku (dla servletu prezentacji)
		ses.setAttribute( "StatusCode" , new Integer(command.getStatusCode()));
		
		//Wyniki będą dostępne jako atrybut sesji
		ses.setAttribute("Results" , results);
		ses.setAttribute("Lock", mainLock); // zapisujemy lock, aby mozna go bylo otworzyc pozniej
		
		//wywołanie sevletu prezentacji
		disp = context.getRequestDispatcher(presentationServ);
		disp.forward(req, resp);
	}
	
	public void doGet(HttpServletRequest request,
					HttpServletResponse response) throws ServletException, IOException
	{
		serviceRequest(request, response);			
	}
	
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		serviceRequest(request, response);			
	}

}
