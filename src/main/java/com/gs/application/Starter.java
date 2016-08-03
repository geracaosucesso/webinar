package com.gs.application;

import virtuozo.interfaces.StickyPageLayout;

import com.google.gwt.core.client.EntryPoint;
import com.gs.interfaces.HomePage;

public class Starter implements EntryPoint {
  @Override
  public void onModuleLoad() {
    new HomePage().render(StickyPageLayout.create().attach());
  }
}