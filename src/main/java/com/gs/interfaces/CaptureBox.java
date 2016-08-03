package com.gs.interfaces;

import ghostbuster.infra.Configuration;
import ghostbuster.infra.Configuration.Key;
import ghostbuster.infra.Configuration.Values;
import ghostbuster.infra.MailChimpConsumer;
import ghostbuster.infra.MailChimpConsumer.Field;
import ghostbuster.infra.MailChimpConsumer.MailChimpResponse;
import ghostbuster.infra.MailChimpConsumer.Value;
import virtuozo.infra.EmailValidator;
import virtuozo.interfaces.Animate;
import virtuozo.interfaces.Container;
import virtuozo.interfaces.HasComponents;
import virtuozo.interfaces.InputText;
import virtuozo.interfaces.RichButton;
import virtuozo.interfaces.Row;
import virtuozo.interfaces.Tag;
import virtuozo.interfaces.ViewPort;
import virtuozo.interfaces.css.ButtonColor;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CaptureBox {
  private Value email = Value.create(Field.EMAIL);
  
  private InputText emailInput;
  
  private RichButton submit;
  
  private String user;
  
  private String id;
  
  private Tag<DivElement> message = Tag.asDiv().hide().css("message");
  
  public static CaptureBox create(){
    Values values = Configuration.get().get(Configuration.Entries.mailchimp);
    return new CaptureBox(values.stringValue(Keys.user), values.stringValue(Keys.id));
  }
  
  public static CaptureBox create(String user, String id){
    return new CaptureBox(user, id);
  }
  
  static enum Keys implements Key{
    user, id
  }
  
  private CaptureBox(String user, String id) {
    this.user = user;
    this.id = id;
        
    this.init();
  }
  
  public CaptureBox attachTo(HasComponents<?, ?> parent) {
    Container container = Container.fluid().attachTo(parent);
    Row row = container.addRow();
    row.addColumn().span(8, ViewPort.SMALL).span(12, ViewPort.X_SMALL).add(this.emailInput);
    row.addColumn().span(4, ViewPort.SMALL).span(12, ViewPort.X_SMALL).add(this.submit);
    container.addRow().addColumn().span(12, ViewPort.X_SMALL).add(this.message);
    return this;
  }
    
  private void init(){
    this.emailInput = InputText.create().placeholder("Coloque aqui seu melhor e-mail").css("form-control");
    this.submit = RichButton.create().text("EU QUERO PARTICIPAR!").css(ButtonColor.SUCCESS);
    
    SubmitHandler handler = new SubmitHandler();
    this.submit.onClick(handler);
  }
  
  class SubmitHandler implements ClickHandler, KeyUpHandler {

    @Override
    public void onClick(ClickEvent event) {
      handle();
    }
    
    @Override
    public void onKeyUp(KeyUpEvent event) {
      if(event.getNativeKeyCode() == 13){
        handle();
      }
    }

    private void handle() {
      if(emailInput.value().isEmpty() || !EmailValidator.get().validate(emailInput.value())){
        message.text("Por favor, preencha o seu melhor e-mail");
        Animate.fadeIn.execute(message.show());
        return;
      }
      
      email.set(emailInput.value());
      
      MailChimpConsumer.create(user, id).post(new AsyncCallback<MailChimpResponse>() {
        @Override
        public void onSuccess(MailChimpResponse result) {
          message.html(result.message().get());
          Animate.fadeIn.execute(message.show());
        }

        @Override
        public void onFailure(Throwable exception) {
          message.html(exception.getMessage());
          Animate.fadeIn.execute(message.show());
        }
      }, email);
    }
    
  }
}