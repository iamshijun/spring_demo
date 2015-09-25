package cjava.walker.spel;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("legalServices")
public class LegalServices {
	
  @Value("#{legalServices.initLegalLimit()}")
  private Calendar legalLimit;
 
  public void initLegalLimit() {
    legalLimit = Calendar.getInstance();
    legalLimit.set(Calendar.YEAR, legalLimit.get(Calendar.YEAR) - 21);
  }
 
  public Calendar getLegalLimit() {
    return legalLimit;
  }
}