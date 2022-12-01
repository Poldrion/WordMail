package service;

import controller.MainWindowController;
import jakarta.xml.bind.JAXBElement;
import model.Department;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static service.Constants.*;
import static service.Utilities.LoadListFiles;


public class CreateFile extends Thread {
    private static MainWindowController mainWindowController;

    public CreateFile(MainWindowController mainWindowController) {
        CreateFile.mainWindowController = mainWindowController;
    }


    @Override
    public void run() {
        // Чтение из файла шаблона в переменную doc
        WordprocessingMLPackage doc = null;
        try {
            doc = getTemplate();
        } catch (FileNotFoundException | Docx4JException e) {
            e.printStackTrace();
        }

        ArrayList<Department> listAddress = new ArrayList<>(MainWindowController.getAddressList());

        // Создание файла docx
        assert doc != null;
        if (listAddress.size() < 5) createTemplateFileForAddress(doc, listAddress);
        else createTemplateFileForMailingList(doc, listAddress);

        // Сохранение переменной doc в новый файл
        String dirToSave;

        if (listAddress.size() > 1) {
            dirToSave = LoadListFiles().get(2) + "\\Список рассылки\\" + mainWindowController.getSubjectMail().trim() + " "
                    + mainWindowController.getDateMail().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".docx";
        } else {
            dirToSave = LoadListFiles().get(2) + "\\" + mainWindowController.getParentName(listAddress.get(0)).trim() + "\\"
                    + listAddress.get(0).getShortName().trim() + "\\" + mainWindowController.getSubjectMail().trim() + " "
                    + mainWindowController.getDateMail().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".docx";
        }
        // Проверка на наличие всех промежуточных папок
        if (!Files.exists(Path.of(LoadListFiles().get(2).trim()))) {
            try {
                Files.createDirectory(Path.of(LoadListFiles().get(2).trim()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        if (listAddress.size() > 1) {
            if (!Files.exists(Path.of(LoadListFiles().get(2).trim() + "\\Список рассылки\\"))) {
                try {
                    Files.createDirectory(Path.of(LoadListFiles().get(2).trim() + "\\Список рассылки\\"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        } else {
            if (!Files.exists(Path.of(LoadListFiles().get(2).trim() + "\\" + mainWindowController.getParentName(listAddress.get(0)).trim()))) {
                try {
                    Files.createDirectory(Path.of(LoadListFiles().get(2).trim() + "\\" + mainWindowController.getParentName(listAddress.get(0)).trim()));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            if (!Files.exists(Path.of(LoadListFiles().get(2).trim() + "\\" + mainWindowController.getParentName(listAddress.get(0)).trim() +
                    "\\" + listAddress.get(0).getShortName().trim() + "\\"))) {
                try {
                    Files.createDirectory(Path.of(LoadListFiles().get(2) + "\\" + mainWindowController.getParentName(listAddress.get(0)).trim() +
                            "\\" + listAddress.get(0).getShortName().trim() + "\\"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

        try {
            writeDocxToStream(doc, dirToSave);
            //  Открытие файла внешней программой
            Desktop.getDesktop().open(new File(dirToSave));
        } catch (IOException | Docx4JException e) {
            e.printStackTrace();
        }

    }

    private void createTemplateFileForAddress(WordprocessingMLPackage doc, ArrayList<Department> listAddress) {
        StringBuilder departmentAndDirector = new StringBuilder();
        for (Department d : listAddress) {
            departmentAndDirector.append(d.getDirector().getAppeal()).append(System.lineSeparator()).append(System.lineSeparator());
        }

        MainDocumentPart mainDocumentPart = doc.getMainDocumentPart();
        mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_ADDRESS, departmentAndDirector.toString());

        addSeparatorRow(mainDocumentPart, DEFAULT_STYLE, 2);

        mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_ATTRIBUTE, "Исх. №СНГ " + mainWindowController.getIdDepartment().getIdDepartment() + "-");
        mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_ATTRIBUTE, "от " + mainWindowController.getDateMail().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        addSeparatorRow(mainDocumentPart, DEFAULT_STYLE, 1);

        mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_SUBJECT, "Касательно ****");

        addSeparatorRow(mainDocumentPart, DEFAULT_STYLE, 2);

        String appeal;
        if (listAddress.size() > 1) {
            appeal = "Уважаемые коллеги!";
        } else {
            appeal = listAddress.get(0).getDirector().isMale() ?
                    "Уважаемый " + listAddress.get(0).getDirector().getFirstName() + " " + listAddress.get(0).getDirector().getPatronymic() + "!" :
                    "Уважаемая " + listAddress.get(0).getDirector().getFirstName() + " " + listAddress.get(0).getDirector().getPatronymic() + "!";
        }

        mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_APPEAL, appeal);

        addSeparatorRow(mainDocumentPart, DEFAULT_STYLE, 1);

        addSeparatorRow(mainDocumentPart, PERSONAL_STYLE_MAIN_TEXT, 8);

        P p = mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_SIGNATORY, mainWindowController.getSignatory().getPost());
        ObjectFactory factory = Context.getWmlObjectFactory();
        R r2 = factory.createR();
        R.Tab rTab = factory.createRTab();
        JAXBElement<R.Tab> rTabWrapped = factory.createRTab(rTab);
        Text text2 = factory.createText();
        JAXBElement<Text> textWrapped2 = factory.createRT(text2);
        text2.setValue(mainWindowController.getSignatory().getNameSignatory());
        r2.getContent().add(rTabWrapped);
        r2.getContent().add(textWrapped2);
        p.getContent().add(r2);

        addSeparatorRow(mainDocumentPart, DEFAULT_STYLE, 7);

        mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_EXECUTOR,
                mainWindowController.getExecutor().getName() +
                        System.lineSeparator() + mainWindowController.getExecutor().getPhone() +
                        System.lineSeparator() + mainWindowController.getExecutor().getEmail());
    }

    private void createTemplateFileForMailingList(WordprocessingMLPackage doc, ArrayList<Department> listAddress) {
        MainDocumentPart mainDocumentPart = doc.getMainDocumentPart();
        mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_ADDRESS, "Согласно листу рассылки" + System.lineSeparator());

        addSeparatorRow(mainDocumentPart, DEFAULT_STYLE, 2);

        mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_ATTRIBUTE, "Исх. №СНГ " + mainWindowController.getIdDepartment().getIdDepartment() + "-");
        mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_ATTRIBUTE, "от " + mainWindowController.getDateMail().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        addSeparatorRow(mainDocumentPart, DEFAULT_STYLE, 1);

        mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_SUBJECT, "Касательно ****");

        addSeparatorRow(mainDocumentPart, DEFAULT_STYLE, 2);

        mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_APPEAL, "Уважаемые коллеги!");

        addSeparatorRow(mainDocumentPart, DEFAULT_STYLE, 1);

        addSeparatorRow(mainDocumentPart, PERSONAL_STYLE_MAIN_TEXT, 8);

        P p = mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_SIGNATORY, mainWindowController.getSignatory().getPost());
        ObjectFactory factory = Context.getWmlObjectFactory();
        R r2 = factory.createR();
        R.Tab rTab = factory.createRTab();
        JAXBElement<R.Tab> rTabWrapped = factory.createRTab(rTab);
        Text text2 = factory.createText();
        JAXBElement<Text> textWrapped2 = factory.createRT(text2);
        text2.setValue(mainWindowController.getSignatory().getNameSignatory());
        r2.getContent().add(rTabWrapped);
        r2.getContent().add(textWrapped2);
        p.getContent().add(r2);

        addSeparatorRow(mainDocumentPart, DEFAULT_STYLE, 7);

        mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_EXECUTOR,
                mainWindowController.getExecutor().getName() +
                        System.lineSeparator() + mainWindowController.getExecutor().getPhone() +
                        System.lineSeparator() + mainWindowController.getExecutor().getEmail());

        Br br = new Br();
        br.setType(STBrType.PAGE);
        P p1 = factory.createP();
        p1.getContent().add(br);
        mainDocumentPart.getJaxbElement().getBody().getContent().add(p1);

        mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_APPEAL, "Список рассылки");

        addSeparatorRow(mainDocumentPart, DEFAULT_STYLE, 1);

        for (Department d : listAddress) {
            mainDocumentPart.addStyledParagraphOfText(PERSONAL_STYLE_MAILING_LIST,d.getNameProperty());
        }

    }

    private void addSeparatorRow(MainDocumentPart mainDocumentPart, String defaultStyle, int count) {
        for (int i = 0; i < count; i++) {
            mainDocumentPart.addStyledParagraphOfText(defaultStyle, System.lineSeparator());
        }
    }

    private WordprocessingMLPackage getTemplate() throws Docx4JException, FileNotFoundException {
        return WordprocessingMLPackage.load(new FileInputStream(TEMPLATE_FILE));
    }

    private void writeDocxToStream(WordprocessingMLPackage template, String target) throws IOException, Docx4JException {
        File f = new File(target);
        template.save(f);
    }


}
