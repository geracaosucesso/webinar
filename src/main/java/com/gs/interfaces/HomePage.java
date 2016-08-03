package com.gs.interfaces;

import virtuozo.infra.AsyncException;
import virtuozo.infra.HttpClient;
import virtuozo.infra.HttpClient.Endpoint;
import virtuozo.infra.TextCallback;
import virtuozo.interfaces.Container;
import virtuozo.interfaces.Heading;
import virtuozo.interfaces.Navbar;
import virtuozo.interfaces.Paragraph;
import virtuozo.interfaces.RichImage;
import virtuozo.interfaces.Row;
import virtuozo.interfaces.Row.Column;
import virtuozo.interfaces.StickyPageLayout;
import virtuozo.interfaces.Thumbnail;
import virtuozo.interfaces.ViewPort;

public class HomePage {

  public void render(StickyPageLayout layout) {
    Container container = Container.fluid().attachTo(layout.body());
    
    container.addRow().addColumn().span(12, ViewPort.X_SMALL).add(RichImage.create().source("img/logo.png").css("logo").responsive());
    
    final Paragraph description = Paragraph.create().css("description");
    container.addRow().addColumn().span(12, ViewPort.X_SMALL).add(Heading.two().text("Super Webinar - 04/08/16 às 20:30").css("heading")).add(description);
    
    HttpClient.create(Endpoint.create("/description.html")).get().send(new TextCallback() {
      @Override
      public void onSuccess(String response) {
        description.html(response);
      }
      
      @Override
      public void onFailure(AsyncException exception) {
        
      }
    });
    
    Row guests = container.addRow().css("guests");
    
    this.addGuest(guests.addColumn(), "img/vitorq.jpg", "Vitor Queiroz", "Agile Coach e Organizador do Caipira Ágil");
    this.addGuest(guests.addColumn(), "img/ftmamud.jpg", "Felipe Mamud", "Artesão de Software e Organizador do Friends Tech Day");
    this.addGuest(guests.addColumn(), "img/renatorfr.jpg", "Renato Freire", "Java Developer e Organizador do Caipira Ágil");
    
    CaptureBox.create().attachTo(layout.footer().css(Navbar.Type.INVERSE));
  }
  
  private void addGuest(Column column, String avatar, String name, String description){
    column.span(4, ViewPort.SMALL).span(12, ViewPort.X_SMALL);
    Thumbnail thumbnail = Thumbnail.create().attachTo(column).css("guest");
    thumbnail.image().source(avatar).css(RichImage.Shape.ROUNDED).responsive();
    thumbnail.caption().add(Heading.three().text(name)).add(Paragraph.create().text(description));
  }
}