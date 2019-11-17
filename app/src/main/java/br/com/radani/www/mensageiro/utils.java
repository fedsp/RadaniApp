package br.com.radani.www.mensageiro;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

/** Métodos auxiliares */

public class utils {
    /**
     * Método geral para a saída de mensagens de depuração para o log
     */
    public static void log(String message) {
        if (BuildConfig.DEBUG) {
            if (message != null) Log.i(Constants.TAG, message);
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
     * achar o Modulo de um valor
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
        return Integer.toHexString(utils.mod(crc, 256));
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
        return settings.getString(item, Constants.TAG);
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
        byte[] result = (bolha ? utils.toHex(commandString) : commandString.getBytes());

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
            case "0":
                if (intcodigo <= 9) {
                    codigo = String.valueOf(intcodigo);
                    codigo = "SI" + "0" + codigo;
                    break;
                } else {
                    codigo = String.valueOf(intcodigo);
                    codigo = "SI" + codigo;
                    break;
                }
            case "1":
                if (intcodigo <= 9) {
                    codigo = String.valueOf(intcodigo);
                    codigo = "SC" + "0" + codigo;
                    break;
                } else {
                    codigo = String.valueOf(intcodigo);
                    codigo = "SC" + codigo;
                    break;
                }
            case "2":
                if (intcodigo <= 9) {
                    codigo = String.valueOf(intcodigo);
                    codigo = "SF" + "0" + codigo;
                    break;
                } else {
                    codigo = String.valueOf(intcodigo);
                    codigo = "SF" + codigo;
                    break;
                }
            case "3":
                if (intcodigo <= 9) {
                    codigo = String.valueOf(intcodigo);
                    codigo = "S1" + "0" + codigo;
                    break;
                } else {
                    codigo = String.valueOf(intcodigo);
                    codigo = "S1" + codigo;
                    break;
                }
            case "4":
                if (intcodigo <= 9) {
                    codigo = String.valueOf(intcodigo);
                    codigo = "S2" + "0" + codigo;
                    break;
                } else {
                    codigo = String.valueOf(intcodigo);
                    codigo = "S2" + codigo;
                    break;
                }
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
            case "7":
                if (intcodigo <= 9) {
                    codigo = String.valueOf(intcodigo);
                    codigo = "D" + "0" + codigo;
                    break;
                } else {
                    codigo = String.valueOf(intcodigo);
                    codigo = "D" + codigo;
                    break;
                }
            default:
                break;
        }
        return codigo;
    }

    public static String obterValor(String hexatual,String tipoDado)
    {
        if (tipoDado.equals("0") | tipoDado.equals("1") | tipoDado.equals("2") | tipoDado.equals("3") | tipoDado.equals("4")) {
            String valor;
            valor = hexatual.substring(9, 10);

//            Integer valornumero;
//            valornumero = Integer.parseInt(valor, 16);
//            valor = String.valueOf(valornumero);
            return valor;
        }
        // Aqui é um tratamento para se o dado não for do tipo 0,1,2,3 ou 4 (estou debugando o valor c04, então ele é do tipo 6)
        else {
            //declara uma string vazia para receber o valor
            String valor;
            //atribuo uma 'fatia' do código hexadecimal, entre as posições 8 e 10.
            valor = hexatual.substring(8, 10);
            //declaro uma variável vazia que vai receber o valor decimal correspondente ao hexa obtido acima
            Integer valornumero;
            //uso uma função que converte o valor de base 16 para base 10
            valornumero = Integer.parseInt(valor, 16);
            //transformo o valor recebido em string, por conveniência
            valor = String.valueOf(valornumero);
            //retorno a string
            return valor;
        }
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

    public static String obterTipoSequencia(String hexatual)
    {
        String tipoSequencia;
        tipoSequencia = hexatual.substring(9,10);
        return tipoSequencia;
    }

    public static String obterValorSequencia(String hexatual)
    {
        String tipoSequencia;
        tipoSequencia = hexatual.substring(9,10);
        String valorSequencia;
        valorSequencia = hexatual.substring(8,9);
        if (tipoSequencia.equals("0") || tipoSequencia.equals("1") || tipoSequencia.equals("2") || tipoSequencia.equals("3")){
            valorSequencia = "";
        }
        else {
            valorSequencia = ": "+valorSequencia;
        }
        return valorSequencia;
    }

    public static String obterLabelSequencia(String tipoSequencia) {
        switch (tipoSequencia) {
            case "0":
                tipoSequencia = "Fim da Sequência";
                break;
            case "1":
                tipoSequencia = "Coleta";
                break;
            case "2":
                tipoSequencia = "Impressão";
                break;
            case "3":
                tipoSequencia = "Mistura";
                break;
            case "4":
                tipoSequencia = "Aguarda Entrada ";
                break;
            case "5":
                tipoSequencia = "Libera Entrada ";
                break;
            case "6":
                tipoSequencia = "Alterna Saída ";
                break;
            case "7":
                tipoSequencia = "Aguarda Tempo ";
                break;
            default:
                break;
        }
        return tipoSequencia;
    }



    public static String trata_valor_multiplicador (String valor, Double multiplicador) {
        switch (String.valueOf(multiplicador)) {
            case "10.0":

                Integer decimal_int_value;
                String[] result;
                result = valor.split("\\.");
                String decimal_string_value = result[result.length - 1];
                try {
                    decimal_int_value = Integer.parseInt(decimal_string_value);
                    if (decimal_int_value == 0) {
                        valor = result[result.length - 2];
                    } else {
                    }
                } catch (Exception e) {
                }
                break;
            case "1.0":
                result = valor.split("\\.");
                decimal_string_value = result[result.length - 1];
                try {
                    decimal_int_value = Integer.parseInt(decimal_string_value);
                    if (decimal_int_value == 0) {
                        valor = result[result.length - 2];
                    } else {
                    }
                } catch (Exception e) {
                }
                break;
            case "0.1":
                String result_case1[];
                result_case1 = valor.split("\\.");
                try {
                    result_case1[1] = result_case1[1] + "00000";
                    valor = result_case1[0] +"."+result_case1[1];
                } catch (Exception e) {
                    valor = valor+".00000";
                }
                String result_case1_2[];
                result_case1_2 = valor.split("\\.");
                result_case1_2[1] = result_case1_2[1].substring(0, 1);
                valor = result_case1_2[0] +"."+ result_case1_2[1];
                break;
            case "0.01":
                String result_case2[];
                result_case2 = valor.split("\\.");
                try {
                    result_case2[1] = result_case2[1] + "00000";
                    valor = result_case2[0] +"."+result_case2[1];
                } catch (Exception e) {
                    valor = valor+".00000";
                }
                String result_case2_2[];
                result_case2_2 = valor.split("\\.");
                result_case2_2[1] = result_case2_2[1].substring(0, 2);
                valor = result_case2_2[0] +"."+ result_case2_2[1];
                break;
        }
        return valor;
    }
}






