package ebookshop;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import modelo.Book;


public class Controlador extends HttpServlet {
	private Vector<Book> shoplist = null;

  public void init(ServletConfig conf) throws ServletException  {
    super.init(conf);
    }

  public void doGet (HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    doPost(req, res);
    }

  public void doPost (HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    HttpSession session = req.getSession(true);
    this.shoplist = (Vector<Book>)session.getAttribute("carrito");
    if (this.shoplist == null) { // primera vezz
    	shoplist = new Vector<Book>();
    	session.setAttribute("carrito", shoplist);
    }
    String do_this = req.getParameter("do_this");
    String rutaJSP = "/";
    if (do_this == null) {
    	cargarLibros(session);
    } else {
    	
    }
    switch (do_this) {
    	case "checkout":
    		facturar(req);
    		rutaJSP = "/Checkout.jsp";
    		break;
    	case "remove":
    		quitarLibros(req);
    		break;
    	case "add":
    		addLibros(req);
    }
    
    ServletContext sc = getServletContext();
    RequestDispatcher rd = sc.getRequestDispatcher(rutaJSP);
    rd.forward(req, res);
    } // doPost

  private void cargarLibros(HttpSession session) {
	  Vector<String> blist = new Vector<String>();
      
      blist.addElement("Beginning JSP, JSF and Tomcat. Zambon/Sekler $39.99");
      blist.addElement("Beginning JBoss Seam. Nusairat $39.99");
      blist.addElement("Founders at Work. Livingston $25.99");
      blist.addElement("Business Software. Sink $24.99");
      blist.addElement("Foundations of Security. Daswani/Kern/Kesavan $39.99");
      session.setAttribute("ebookshop.list", blist);
  }
  
  private void facturar(ServletRequest req) {
      float dollars = 0;
      int   books = 0;
      for (int i = 0; i < shoplist.size(); i++) {
        Book  aBook = (Book)shoplist.elementAt(i);
        float price = aBook.getPrice();
        int   qty = aBook.getQuantity();
        dollars += price * qty;
        books += qty;
        }
      req.setAttribute("dollars", new Float(dollars).toString());
      req.setAttribute("books", new Integer(books).toString());
  }
  
  private void quitarLibros(ServletRequest req) {
	  String pos = req.getParameter("position");
      shoplist.removeElementAt((new Integer(pos)).intValue());
  }
  
  private void addLibros(HttpServletRequest req) {
	    boolean found = false;
        Book aBook = getBook(req);
        if (shoplist == null) {  // the shopping cart is empty
          shoplist = new Vector<Book>();
          shoplist.addElement(aBook);
          }
        else {  // update the #copies if the book is already there
          for (int i = 0; i < shoplist.size() && !found; i++) {
            Book b = (Book)shoplist.elementAt(i);
            if (b.getTitle().equals(aBook.getTitle())) {
              b.setQuantity(b.getQuantity() + aBook.getQuantity());
              shoplist.setElementAt(b, i);
              found = true;
              }
            } // for (i..
          if (!found) {  // if it is a new book => Add it to the shoplist
            shoplist.addElement(aBook);
            }
          } // if (shoplist == null) .. else ..
  }


}
  
 