package zad1;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//SERWLET POBIERANIA PARAMETRÓW

public class GetParamsServ extends HttpServlet {
	private ServletContext context;
	private String resBundleServ; // nazwa serwletu przygotowującego
									// sparametryzowaną informacje

	// Inicjacja
	public void init() {
		context = getServletContext();
		resBundleServ = context.getInitParameter("resBundleServ");
	}

	// Obsługa zleceń
	public void serviceRequest(HttpServletRequest req,
                          HttpServletResponse resp) throws ServletException, IOException
    {
		// Włączenie serwletu przygotowującego informacje z zasobów (ResourceBundle). Informacja będzie dostępna poprzez statyczne metody klasy BundleInfo
		RequestDispatcher disp = context.getRequestDispatcher(resBundleServ);
		disp.include(req, resp);

		// Pobranie potrzebnej informacji
		// ktora została wczesniej przygotowana
		// przez klasę BundleInfo na podstawie zlokalizowanych zasobów

		
		String charset = BundleInfo.getCharset(); // Zlokalizowana strona kodowa
		String[] headers = BundleInfo.getHeaders(); // Napisy nagłówkowe
		String[] pnames = BundleInfo.getCommandParamNames();// Nazwy parametrów (pojawią się w formularzu, ale również są to nazwy parametrów dla Command)		
		String[] pdes = BundleInfo.getCommandParamDescr();// Opisy parametrów - aby było wiadomo co w formularzu wpisywać
		String submitMsg = BundleInfo.getSubmitMsg();// Napis na przycisku
		String[] footers = BundleInfo.getFooters();// Ew. końcowe napisy na stronie

		req.setCharacterEncoding(charset);// Ustalenie właściwego kodowania zlecenia - bez tego nie będzie można własciwie odczytać parametrów

		HttpSession session = req.getSession();// Pobranie aktualnej sesji, w jej atrybutach są/będą przechowywane, wartości parametrów

		resp.setCharacterEncoding(charset);// Generowanie strony
		PrintWriter out = resp.getWriter();

		out.println("<center><h2>");
		for (int i = 0; i < headers.length; i++)
			out.println(headers[i]);
		out.println("</h2></center><hr>");

		// formularz
		out.println("<form method=\"post\">");
		for (int i = 0; i < pnames.length; i++) {
			out.println(pdes[i] + "<br>");
			out.print("<input type=\"text\" size=\"60\" name=\"" + pnames[i] + "\"");

			// Jezeli są już wartości parametrów - pokażemy je w formularzu
			String pval = (String) session.getAttribute("param_" + pnames[i]);
			if (pval != null)
				out.print(" value=\"" + pval + "\"");
			out.println("><br>");
		}
		out.println("<br><input type=\"submit\" value=\"" + submitMsg + "\">");
		out.println("</form>");

		// Pobieranie parametrów z formularza

		for (int i = 0; i < pnames.length; i++) {
			String paramVal = req.getParameter(pnames[i]);
			
			if (paramVal == null)// Jeżeli brak parametru (ów) - konczymy
				return;

			session.setAttribute("param_" + pnames[i], paramVal);// Jest parametr - zapiszmy jego wartość jako atrybut sesji.Zostanie on pobrany przez Controller, który ustali te wartości dla wykonania Command
		}
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
