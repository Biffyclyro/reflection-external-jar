import javax.management.Attribute;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ex6 {
    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos JAR", "jar");
        jfc.setFileFilter(filter);

        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File jar = jfc.getSelectedFile();


            URLClassLoader child = new URLClassLoader(new URL[]{jar.toURI().toURL()});


            List<Class<?>> classes = new ArrayList<>();
            classes.add(Class.forName("Banco", true, child));
            classes.add(Class.forName("Conta", true, child));
            classes.add(Class.forName("Movimentacao", true, child));
            Class meuEnum = Class.forName("Conta$Tipo", true, child);

            for(Class<?> c : classes){
                System.out.println(c.getName() + ":");
                System.out.println("Atributos:");
                for (Field f : c.getDeclaredFields()) System.out.println(f.getName() + ": " + f.getType());
                System.out.println();
                System.out.println("Métodos: ");
                for(Method m : c.getDeclaredMethods()) System.out.println(m.getName());
                System.out.println();
            }

            Class<?> testeBanco = classes.get(0);
            Class<?> testeConta = classes.get(1);
            Class<?> testeMovimentacao = classes.get(2);

            Object bancoInstance = classes.get(0)
                    .getConstructor(String.class, Map.class)
                    .newInstance( "Banco", new HashMap<Integer, Object>());

           /* Object contaInstance = classes.get(1)
                    .getDeclaredConstructor()
                    .newInstance();
*/




            Method criarConta = testeBanco
                    .getDeclaredMethod("createConta",
                                        int.class,
                                        boolean.class,
                                        double.class,
                                        meuEnum);

            Method depositarConta = testeBanco
                    .getDeclaredMethod("depositar",
                            int.class,
                            double.class);

            Method scarConta = testeBanco
                    .getDeclaredMethod("sacar",
                            int.class,
                            double.class);

            Method extratoConta = testeBanco
                    .getDeclaredMethod("tirarEstrato",
                            int.class);



            criarConta.invoke(bancoInstance,1, true, 1000, Enum.valueOf(meuEnum, "RENDA_FIXA") );

            depositarConta.invoke(bancoInstance, 1, 100);
            scarConta.invoke(bancoInstance, 1, 1);
            extratoConta.invoke(bancoInstance, 1);


        }


    }
}
