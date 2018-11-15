package ru.ifmo.prog.lab8;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LabLocale {
    private static LabLocale instance = null;

    public static LabLocale getInstance() {
        return instance == null ? instance = new LabLocale() : instance;
    }

    private Locale currentLocale = null;
    private ResourceBundle resourceBundle = null;
    private List<LabLocaleUpdateHandler> labLocaleUpdateHandlers = null;

    private LabLocale() {
        currentLocale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle("ru.ifmo.prog.lab8.SyntaxBundle", currentLocale);
        labLocaleUpdateHandlers = new ArrayList<>();
    }

    public JMenu getLocalesMenu() {
        JMenu languages = new JMenu(resourceBundle.getString("Language"));
        JMenuItem ruLanguageButton = languages.add(new JMenuItem(resourceBundle.getString("Russian")));
        JMenuItem enLanguageButton = languages.add(new JMenuItem(resourceBundle.getString("English")));
        JMenuItem slovLanguageButton = languages.add(new JMenuItem(resourceBundle.getString("Slovenian")));
        JMenuItem hungLanguageButton = languages.add(new JMenuItem(resourceBundle.getString("Hungarian")));

        addLocaleChangeHandler(bundle -> {
            languages.setText(resourceBundle.getString("Language"));
            ruLanguageButton.setText(resourceBundle.getString("Russian"));
            enLanguageButton.setText(resourceBundle.getString("English"));
            slovLanguageButton.setText(resourceBundle.getString("Slovenian"));
            hungLanguageButton.setText(resourceBundle.getString("Hungarian"));
        });

        ruLanguageButton.addActionListener(event -> setLocale(new Locale("ru")));
        enLanguageButton.addActionListener(event -> setLocale(new Locale("en", "AU")));
        slovLanguageButton.addActionListener(event -> setLocale(new Locale("sl")));
        hungLanguageButton.addActionListener(event -> setLocale(new Locale("hu")));

        return languages;
    }

    public void addLocaleChangeHandler(LabLocaleUpdateHandler labLocaleUpdateHandler) {
        labLocaleUpdateHandlers.add(labLocaleUpdateHandler);
    }

    public String getString(String key) {
        return resourceBundle.getString(key);
    }

    private void setLocale(Locale locale) {
        Locale.setDefault(locale);
        currentLocale = locale;
        resourceBundle = ResourceBundle.getBundle("ru.ifmo.prog.lab8.SyntaxBundle", currentLocale);
        if (labLocaleUpdateHandlers != null)
            labLocaleUpdateHandlers.forEach(labLocaleUpdateHandler -> labLocaleUpdateHandler.handle(resourceBundle));
    }

    public Locale getLocale() {
        return currentLocale;
    }
}
