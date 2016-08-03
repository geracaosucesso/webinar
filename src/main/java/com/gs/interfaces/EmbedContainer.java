package com.gs.interfaces;

import virtuozo.infra.Elements;
import virtuozo.interfaces.Composite;

public class EmbedContainer extends Composite<EmbedContainer> {
  public EmbedContainer() {
    super(Elements.div());
    this.css("embed-container");
  }
}
