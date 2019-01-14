package br.com.radani.www.mensageiro;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;




/**
 * Métodos auxiliares
 * Created by sash0k on 29.01.14.
 */
public class Utils {
    /**
     * Método geral para a saída de mensagens de depuração para o log
     */
    public static void log(String message) {
        if (BuildConfig.DEBUG) {
            if (message != null) Log.i(Const.TAG, message);
        }
    }
    // ============================================================================


    /**
     * Converte comandos em hexa para string para exibição
     */
    public static String printHex(String hex) {
        StringBuilder sb = new StringBuilder();
        int len = hex.length();
        try {
            for (int i = 0; i < len; i += 2) {
                sb.append("0x").append(hex.substring(i, i + 2)).append(" ");
            }
        } catch (NumberFormatException e) {
            log("printHex NumberFormatException: " + e.getMessage());

        } catch (StringIndexOutOfBoundsException e) {
            log("printHex StringIndexOutOfBoundsException: " + e.getMessage());
        }
        return sb.toString();
    }
    // ============================================================================


    /**
     * Converte comandos em ascii para hex byte por byte
     * @param hex - Time
     * @return - Array de bytes de comando
     */
    public static byte[] toHex(String hex) {
        int len = hex.length();
        byte[] result = new byte[len];
        try {
            int index = 0;
            for (int i = 0; i < len; i += 2) {
                result[index] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
                index++;
            }
        } catch (NumberFormatException e) {
            log("toHex NumberFormatException: " + e.getMessage());

        } catch (StringIndexOutOfBoundsException e) {
            log("toHex StringIndexOutOfBoundsException: " + e.getMessage());
        }
        return result;
    }
    // ============================================================================


    /**
     * Método para mesclar dois arrays em um
     */
    public static byte[] concat(byte[] A, byte[] B) {
        byte[] C = new byte[A.length + B.length];
        System.arraycopy(A, 0, C, 0, A.length);
        System.arraycopy(B, 0, C, A.length, B.length);
        return C;
    }
    // ============================================================================


    /**
     * Modulo
     */
    public static int mod(int x, int y) {
        int result = x % y;
        return result < 0 ? result + y : result;
    }
    // ============================================================================

    /**
     * Calculo de checksum
     */
    public static String calcModulo256(String command)
    {
        int crc = 0;
        for (int i = 0; i< command.length(); i++) {
            crc += (int)command.charAt(i);
        }
        return Integer.toHexString(Utils.mod(crc, 256));
    }
    // ============================================================================

    /**
     * Colore o texto na cor desejada
     */
    public static String mark(String text, String color) {
        return "<font color=" + color + ">" + text + "</font>";
    }
     // ============================================================================

    /**
     * Pega o ID guardado no toy soundset
     */
    public static String getPrefence(Context context, String item) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(item, Const.TAG);
    }
    // ============================================================================


    /**
     * Flag das configurações
     */
    public static boolean getBooleanPrefence(Context context, String tag) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(tag, true);
    }
    // ============================================================================


    /**
     * Classe de filtro de input
     */
    // ============================================================================
    public static class InputFilterHex implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))
                        && source.charAt(i) != 'A' && source.charAt(i) != 'D'
                        && source.charAt(i) != 'B' && source.charAt(i) != 'E'
                        && source.charAt(i) != 'C' && source.charAt(i) != 'F'
                        ) {
                    return "";
                }
            }
            return null;
        }
    }
    // ============================================================================

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }





    public static byte[] comando(String command)
    {
        final boolean bolha;
        bolha = true;
        String commandString = command;
        // Adição de comandos em HEX
        if (commandString.length() % 2 == 1) {
            commandString = "0" + commandString;
        }
        byte[] result = (bolha ? Utils.toHex(commandString) : commandString.getBytes());

        return result;
    }

    public static String obterHeader(String hexatual)
    {
        String header;
        header = hexatual.substring(2,4);
        Integer valornumero;
        valornumero = Integer.parseInt(header,16);
        header = String.valueOf(valornumero);
        return header;
    }


    public static String obterCodigo(String hexatual, String tipoDado)
    {
        String codigo;
        Integer intcodigo;
        codigo = hexatual.substring(6,8);
        intcodigo = Integer.parseInt(codigo,16);

        switch (tipoDado) {
            case "5":
                if (intcodigo <= 9) {
                    codigo = String.valueOf(intcodigo);
                    codigo = "P" + "0" + codigo;
                    break;
                } else {
                    codigo = String.valueOf(intcodigo);
                    codigo = "P" + codigo;
                    break;
                }
            case "6":
                if (intcodigo <= 9) {
                    codigo = String.valueOf(intcodigo);
                    codigo = "C" + "0" + codigo;
                    break;
                } else {
                    codigo = String.valueOf(intcodigo);
                    codigo = "C" + codigo;
                    break;
                }
            default:
                break;
        }
        return codigo;
    }

    public static String obterValor(String hexatual)
    {
        String valor;
        valor = hexatual.substring(8,10);
        Integer valornumero;
        valornumero = Integer.parseInt(valor,16);
        valor = String.valueOf(valornumero);
        return valor;
    }

    public static String obterTipoDado(String hexatual)
    {
        String valor;
        valor = hexatual.substring(2,3);
        Integer valornumero;
        valornumero = Integer.parseInt(valor,16);
        valor = String.valueOf(valornumero);
        return valor;
    }


}






