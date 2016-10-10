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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import tw.edu.sju.ee.eea.utils.io.tools.EEAException;
import tw.edu.sju.ee.eea.utils.io.tools.EEAInput;
import tw.edu.sju.ee.eea.utils.io.ValueInputStream;
import tw.edu.sju.ee.eea.jni.mps.MPS140801;
import tw.edu.sju.ee.eea.utils.io.ChannelInputStream;
import tw.edu.sju.ee.eea.utils.io.tools.IEPEPlayer;
import tw.edu.sju.ee.eea.utils.io.tools.InputChannel;

/**
 *
 * @author Leo
 */
public class SystemPass {

    public static void main(String[] args) {

        try {
            
            IEPEPlayer player = new IEPEPlayer(16000, 16, 1, 2, 16000);
            Thread playThread = new Thread(player);
            playThread.start();
            OutputStream playOut = player.getOutputStream();
            
            EEAInput iepe = new EEAInput(new MPS140801(0, 16000), new int[]{1});
            ChannelInputStream iepeStream = new ChannelInputStream();
            iepe.getIOChannel(1).addStream(iepeStream);
            Thread thread = new Thread(iepe);
            thread.start();
//            IepeInputStream iepeStreams = iepe.getIepeStreams(0);
//            VoltageInputStream vi_left = new VoltageInputStream(iepe.getIepeStreams(0));

//            File file = new File("rec3.iepe");
//            file.createNewFile();
//            FileOutputStream fos = new FileOutputStream(file);

            for (int i = 0; i < 10000; i++) {
                byte[] buffer = new byte[64];
                iepeStream.read(buffer);
                playOut.write(buffer);
//                System.out.println(Arrays.toString(buffer));
//                fos.write(buffer, 0, buffer.length);
            }
//            fos.close();

            thread.stop();
            playThread.stop();
        } catch (IOException ex) {
            Logger.getLogger(TestStream.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}