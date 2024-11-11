import javax.sound.midi.*;

/*music may use for the game and could learn how to do multi threading*/

public class Midi8BitBrainwash {
    public static void main(String[] args) {
        try {
            // get synthesizer
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();

            // get MiDI
            MidiChannel[] channels = synthesizer.getChannels();
            MidiChannel channel = channels[0];

            // Acoustic Bass(32)
            channel.programChange(32);


            int volume = 100;
            int noteDuration = 200;
            int pauseDuration = 50;


            int[] melodyNotes = {
                    0,00,60, 60, 62, 62, 60, 64, 0, 0, // C,C,D,D,C,E.
                    60, 60, 62, 62, 60, 67, 00,00, //
                    67, 67, 69, 69, 67, 72, 00,00,
                    72, 72, 71, 69, 71, 67, 00,00,
                    69, 69, 64, 64, 62, 60, 00,00,
                    60, 60, 62, 62, 60, 64, 67, 67, 64, 60, 00,
                    60, 60, 62, 62, 60, 60 ,00, 60, 57, 64,62,00,00,00,00,00,00,00,00,

                    // 变奏
                    60, 62, 64, 65, 67, 69, 71, 72, // C, D, E, F, G, A, B, C (上升音阶)
                    72, 71, 69, 67, 65, 64, 62, 60, // C, B, A, G, F, E, D, C (下降音阶)
                    64, 64, 65, 67, 00, 67, 65, 64, // E, E, F, G, - , G, F, E (小变化)
                    62, 64, 67, 00, 67, 64, 62, 60, // D, E, G, - , G, E, D, C (结束部分)
                    67, 69, 67, 00, 64, 62, 64, 00, 00,00,00,00,// G, A, G, F, E, D, E, D
                    60, 64, 67, 60, 00, 60, 57, 55, 67,00,00, // 中央C, -, C, A, E, D, - , -
                    60, 00, 62, 00, 64, 00, 65, 00, // C, -, D, -, E, -, F, -
                    67, 67, 00, 67, 64, 62, 60, 64, // G, G, -, G, E, -, E, D
                    64, 67, 67, 00, 65, 67, 69, 72, // 中央C, C, D, E, F, G, A, 高音C
                    00, 72, 00, 71, 69, 67, 65, 64, // -, 高音C, -, B, A, G, F, E
                    62,60,00,00,00,00,00,00,00,00,00,00 // 中央C，作为结束


            };


            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis()>0) {
                for (int note : melodyNotes) {
                    // 播放音符
                    channel.noteOn(note, volume);
                    Thread.sleep(noteDuration);
                    channel.noteOff(note);

                    // 音符之间的短暂停顿
                    Thread.sleep(pauseDuration);
                }
            }

            // 关闭合成器
            synthesizer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
