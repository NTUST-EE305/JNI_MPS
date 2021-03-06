/*
 * Copyright (C) 2014 Leo
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package tw.edu.sju.ee.eea.jni.eea.jni.mps140801;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import tw.edu.sju.ee.eea.utils.io.tools.EEAInput;
import tw.edu.sju.ee.eea.jni.mps.MPS140801;
import tw.edu.sju.ee.eea.utils.io.ChannelInputStream;

/**
 *
 * @author Leo
 */
public class WriteFile {

    public static void main(String[] args) {

        try {
            EEAInput iepe = new EEAInput(new MPS140801(0, 16000), new int[]{1});

            ChannelInputStream iepeStream[] = new ChannelInputStream[8];
            for (int i = 0; i < iepeStream.length; i++) {
                iepeStream[i] = new ChannelInputStream();
                iepe.getIOChannel(i).addStream(iepeStream[i]);
            }

            Thread thread = new Thread(iepe);
            thread.start();
//            IepeInputStream iepeStreams = iepe.getIepeStreams(0);
//            VoltageInputStream vi_left = new VoltageInputStream(iepe.getIepeStreams(0));

//            File file = new File("text.adc");
//            file.createNewFile();
////            FileOutputStream fos = new FileOutputStream(file);
//            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

            while (!Thread.interrupted()) {
                File file = new File(sdFormat.format(new Date()) + ".adc");
                file.createNewFile();
                PrintWriter pw = new PrintWriter(file);
                for (int i = 0; i < 16000 * 60; i++) {
                    for (int j = 0; j < iepeStream.length; j++) {
                        pw.print(iepeStream[j].readValue());
                        pw.print("\t");
                    }
                    pw.print("\n");
//                byte[] buffer = new byte[64];
//                    double readValue = iepeStream.readValue();
//                    System.out.println(readValue);
//                System.out.println(Arrays.toString(buffer));
//                fos.write(buffer, 0, buffer.length);
                }
                pw.close();
            }
//            fos.close();

            thread.stop();
        } catch (IOException ex) {
            Logger.getLogger(TestStream.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
