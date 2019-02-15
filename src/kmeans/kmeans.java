/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kohonen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author brian
 */
public class kkmeans {

    public static void main(String[] args) {
        try {
            int k = 2;
            int igualdad = 1;
            int ctmp = 0;
//-----------------------------------------------------------------------------------
            ArrayList<ArrayList<Double>> centroides = new ArrayList<ArrayList<Double>>();;
            while (igualdad > 0) {
                ArrayList<ArrayList<Integer>> datos = new ArrayList<ArrayList<Integer>>();
                //centroides = new ArrayList<ArrayList<Double>>();
                ArrayList<ArrayList<Double>> centroidesNew = new ArrayList<ArrayList<Double>>();
                ArrayList<ArrayList<Double>> operaciones = new ArrayList<ArrayList<Double>>();
                ArrayList<ArrayList<Double>> operaciones2 = new ArrayList<ArrayList<Double>>();
                ArrayList<Integer> min = new ArrayList<>();
                ArrayList<Integer> val = new ArrayList<>();

                FileReader fr = null;
                BufferedReader br = null;
                String ruta = "E:";
                File archivo = new File(ruta, "kmeansexamen.csv");
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);

                String s = br.readLine();
                String[] p = s.split(";");
                int cantDatos = 0;
                int cantVariables = p.length - 1;

                while ((s = br.readLine()) != null) {
                    cantDatos++;
                }

                int rnd;
                for (int i = 0; i < k; i++) {
                    rnd = (int) (Math.random() * (cantDatos - k)) + 1;
                    if (val.size() > 0) {
                        if (rnd == val.get(val.size() - 1)) {
                            rnd++;
                        }
                    }
                    val.add(rnd);
                    if(ctmp==0)System.out.println("C " + rnd);
                }

                //CREO ARRAYS DE LA CANTIDAD DE VARIABLES Y CENTROIDES
                for (int i = 0; i < cantVariables; i++) {
                    datos.add(new ArrayList<>());
                }

                for (int i = 0; i < k; i++) {
                    ArrayList<Double> x = new ArrayList<>();
                    for (int j = 0; j < cantDatos; j++) {
                        x.add(0.0);
                    }
                    operaciones.add(x);
                }
                for (int i = 0; i < cantDatos; i++) {
                    operaciones2.add(new ArrayList<>());
                }
                for (int i = 0; i < k; i++) {
                    ArrayList<Double> x = new ArrayList<>();
                    for (int j = 0; j < k; j++) {
                        x.add(0.0);
                    }
                    centroidesNew.add(x);
                }

                if (ctmp == 0) {
                    for (int i = 0; i < k; i++) {
                        centroides.add(new ArrayList<>());
                    }

                    fr = new FileReader(archivo);
                    br = new BufferedReader(fr);
                    s = br.readLine();
                    int sum = 1;
                    while ((s = br.readLine()) != null) {
                        p = s.split(";");
                        for (int i = 0; i < val.size(); i++) {
                            if (sum == val.get(i)) {
                                for (int j = 1; j < p.length; j++) {
                                    ArrayList<Double> pr = centroides.get(j - 1);
                                    pr.add(Double.parseDouble(p[j]));
                                    centroides.set(j - 1, pr);
                                }
                            }
                        }
                        sum++;
                    }
                }

                fr = new FileReader(archivo);
                br = new BufferedReader(fr);
                s = br.readLine();
                while ((s = br.readLine()) != null) {
                    p = s.split(";");
                    for (int i = 1; i < p.length; i++) {
                        ArrayList<Integer> pr = datos.get(i - 1);
                        pr.add(Integer.parseInt(p[i]));
                        datos.set(i - 1, pr);
                    }
                }

                for (int j = 0; j < centroides.size(); j++) {
                    ArrayList<Integer> d = datos.get(j);
                    for (int i = 0; i < k; i++) {
                        ArrayList<Double> c = centroides.get(j);
                        for (int l = 0; l < d.size(); l++) {
                            Double valor = Math.sqrt(Math.pow((d.get(l) - c.get(i)), 2));
                            ArrayList<Double> pr = operaciones.get(i);
                            //System.out.print(pr.size() + "-->" + d.get(l) + " - " + c.get(i) + " = (" + valor);
                            //System.out.print(" + " + pr.get(l));
                            pr.set(l, pr.get(l) + valor);
                            operaciones.set(i, pr);
                            //System.out.println(" = " + pr.get(l));
                        }
                        //System.out.println("");
                    }
                    //System.out.println("x");
                }

                for (int i = 0; i < k; i++) {
                    ArrayList<Double> o1 = operaciones.get(i);
                    for (int j = 0; j < cantDatos; j++) {
                        ArrayList<Double> o2 = operaciones2.get(j);
                        o2.add(o1.get(j));
                        operaciones2.set(j, o2);
                        //System.out.println("x->" + i + " " + o2.get(i));
                    }
                    //System.out.println("z");
                }

                for (int i = 0; i < cantDatos; i++) {
                    ArrayList<Double> o1 = operaciones2.get(i);
                    Double n = o1.get(0);
                    int pos = 0;
                    for (int j = 1; j < o1.size(); j++) {
                        if (o1.get(j) < n) {
                            n = o1.get(j);
                            pos = j;
                        }
                    }
                    min.add(pos);
                }

                for (int i = 0; i < cantVariables; i++) {
                    ArrayList<Integer> d = datos.get(i);
                    ArrayList<Double> c = centroidesNew.get(i);
                    for (int l = 0; l < k; l++) {
                        int cont = 0;
                        Double suma = 0.0;
                        for (int j = 0; j < cantDatos; j++) {
                            if (l == min.get(j)) {
                                suma = suma + d.get(j);
                                cont++;
                            }
                        }
                        c.set(l, suma / cont);
                        centroidesNew.set(i, c);
                    }
                }
                igualdad = 0;
                System.out.println("\nITERACIÓN # " + (ctmp+1) + "\n");
                for (int i = 0; i < k; i++) {
                    ArrayList<Double> cn = centroidesNew.get(i);
                    ArrayList<Double> c = centroides.get(i);
                    for (int j = 0; j < k; j++) {
                        System.out.println("        ->" + c.get(j) + "    " + cn.get(j));
                        if (!c.get(j).equals(cn.get(j))) {
                            igualdad++;
                        }
                    }
                }
                

                for (int i = 0; i < k; i++) {
                    ArrayList<Double> cn = centroidesNew.get(i);
                    ArrayList<Double> c = centroides.get(i);
                    for (int j = 0; j < k; j++) {
                        c.set(j, cn.get(j));
                    }
                    centroides.set(i, cn);
                }
                ctmp++;
            }
            System.out.println("\nCENTROIDES:");
            for (int i = 0; i < k; i++) {
                ArrayList<Double> c = centroides.get(i);
                for (int j = 0; j < k; j++) {
                    System.out.println("        ->" + c.get(j));
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(kkmeans.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(kkmeans.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
