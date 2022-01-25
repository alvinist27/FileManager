import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Главный класс программы
public class FManager {
    public static void main(String[] args) {
        Frame myFrame = new MyWindow();
    }
}

// Класс окна приложения
class MyWindow extends Frame implements ActionListener {
    List list;
    Button open;
    Button props;
    Button delete;
    Button add_file;
    Button add_catalog;
    TextField text;

    String current_dir = new File(".").getAbsolutePath().toString();
    Path path = Paths.get(current_dir);
    String dir = path.getParent().toString();
    File file = new File(dir);

    public MyWindow() {
        setLayout(null);
        setSize(700, 500);
        setVisible(true);
        list = new List(30);
        list.setBounds(40, 35, 425, 425);
        add(list);
        list.addActionListener(this);
        PrintCatalog();

        text = new TextField(30);
        text.setBounds(520, 35, 160, 30);
        add(text);

        // Создание кнопки добавления каталога
        add_catalog = new Button("Добавить каталог");
        add_catalog.setBounds(520, 70, 160, 30);
        add(add_catalog);
        add_catalog.addActionListener(this);

        // Создание кнопки добавления файла
        add_file = new Button("Добавить файл");
        add_file.setBounds(520, 105, 160, 30);
        add(add_file);
        add_file.addActionListener(this);

        // Создание кнопки удаления файла
        delete = new Button("Удалить");
        delete.setBounds(520, 140, 160, 30);
        add(delete);
        delete.addActionListener(this);

        // Создание кнопки открытия файла в notepad.exe
        open = new Button("Открыть в notepad.exe");
        open.setBounds(520, 175, 160, 30);
        add(open);
        open.addActionListener(this);

        // Создание кнопки вывода свойств файла
        props = new Button("Вывести свойства файла");
        props.setBounds(520, 210, 160, 30);
        add(props);
        props.addActionListener(this);

        // Закрытие окна
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                ev.getWindow().dispose();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(list)) {
            PickCatalog(list.getSelectedItem());
        }
        else if (e.getSource().equals(add_catalog)) {
            CreateFolder(text.getText());
            PrintCatalog();
        }
        else if (e.getSource().equals(add_file)) {
            CreateFile(text.getText());
            PrintCatalog();
        }
        else if (e.getSource().equals(delete)) {
            Delete(list.getSelectedItem());
            PrintCatalog();
        }
        else if (e.getSource().equals(open)) {
            OpenNotepad(list.getSelectedItem());
        }
        else if (e.getSource().equals(props)) {
            Props(list.getSelectedItem());
        }
    }

    // Функция печати содержимого каталога
    public void PrintCatalog() {
        try {
            if (file.isDirectory()) {
                String[] new_list = file.list();
                list.removeAll();
                if (file.getParent() != null)
                    list.add("...");
                for (int i = 0; i < new_list.length; i++) {
                    list.add(new_list[i]);
                }
            }
        } catch (Exception ex) {
            System.out.println("Ошибка печати содержимого каталога!");
        }
    }

    // Фукнция выбора каталога
    public void PickCatalog(String name) {
        try {
            if (name.equals("...")) {
                file = new File(file.getParent());
            }
            else {
                File newFile = new File(file.getAbsolutePath(), name);
                if (newFile.exists()) {
                    if (newFile.isDirectory()) {
                        file = newFile;
                    }
                }
            }
            PrintCatalog();
        } catch (Exception e) {
            System.out.println("Ошибка выбора каталога!");
        }
    }

    // Фукнция создания нового файла
    public void CreateFile(String name) {
        File newFile = new File(file.getAbsolutePath(), name);
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Ошибка создания нового файла!");
        }
    }

    // Функция создания нового каталога
    public void CreateFolder(String name) {
        File newFile = new File(file.getAbsolutePath(), name);
        try {
            newFile.mkdir();
        } catch (Exception e) {
            System.out.println("Ошибка создания нового каталога!");
        }
    }

    // Фукнция удаления выбранного файла
    public void Delete(String name) {
        try {
            File new_file = new File(file.getAbsolutePath(), name);
            if (new_file.exists()) {
                Files.delete(Paths.get(new_file.getAbsolutePath()));
            }
        } catch(IOException e) {
            System.out.println("Ошибка удаления выбранного файла!");
        }
    }

    // Фукнция чтения содержимого файла путем запуска программы notepad.exe
    public void OpenNotepad(String name) {
        try {
            File newFile = new File(file.getAbsolutePath(), name);
            if (Desktop.isDesktopSupported() && newFile.exists() && newFile.isFile()) {
                try {
                    Desktop.getDesktop().edit(newFile);
                } catch (IOException e) {
                    System.out.println("Ошибка чтения содержимого файла!");
                }
            }
        } catch (Exception ex) {
            System.out.println("Ошибка!");
        }
    }

    // Фукнция распечатки свойств выбранного файла
    public void Props(String name) {
        try {
            if (file.isDirectory()) {
                File new_file = new File(file.getAbsolutePath(), name);
                list.removeAll();
                list.add("Директория? " + (new_file.isDirectory() ? "Да" : "Нет"));
                list.add("Обычный файл? " + (new_file.isFile() ? "Да" : "Нет"));
                list.add("Простое название файла: " + new_file.getName());
                list.add("Полный путь файла: " + new_file.getAbsolutePath());
                list.add("Размер файла: " + new_file.length() + " байт");
                list.add("Доступ к чтению: " + (new_file.canRead() ? "Да" : "Нет"));
                list.add("Доступ к записи: " + (new_file.canWrite() ? "Да" : "Нет"));
            }
        } catch (Exception ex) {
            System.out.println("Ошибка распечатки свойств выбранного файла!");
        }
    }
}
