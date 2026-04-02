package com.recurrence;

import com.recurrence.contract.Recurrence;
import com.recurrence.contract.domain.Coupon;
import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.infra.PageResponse;
import com.recurrence.contract.domain.infra.RequestResponse;
import com.recurrence.pagbank.PagBankEnvironment;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Library {

    private static final String TOKEN = "";

    public static void main(String[] args) {

        RequestResponse<PageResponse<Coupon>> pagBankResponse = Recurrence.PagBank.config(
                        PagBankEnvironment.SANDBOX,
                        TOKEN)
                .coupon().list(0, 10);

        ObjectPrinter.print(pagBankResponse.getBody(),2);
    }

    private static Plan createPagBankPlan() {

        return Plan.builder()
                .name("Nome do plano sdk")
                .referenceId(UUID.randomUUID().toString())
                .amount(500, "BR")
                .description("Descrição do plano")
                .interval(1, "MONTH")
                .paymentMethods(List.of("BOLETO", "CREDIT_CARD"))
                .billingCycles(2)
                .limitSubscriptions(5)
                .setupFee(100)
                .build();
    }

    public class ObjectPrinter {

        // 🎨 CORES
        private static final String RESET = "\u001B[0m";
        private static final String FIELD = "\u001B[33m";   // amarelo
        private static final String VALUE = "\u001B[32m";   // verde
        private static final String TYPE = "\u001B[36m";    // ciano

        public static void print(Object obj) {
            print(obj, 0);
        }

        private static void print(Object obj, int indent) {
            if (obj == null) {
                println(indent, color("null", VALUE));
                return;
            }

            Class<?> clazz = obj.getClass();

            // =========================
            // TIPOS SIMPLES
            // =========================
            if (isPrimitiveLike(obj)) {
                println(indent, color(obj.toString(), VALUE));
                return;
            }

            // =========================
            // COLLECTION
            // =========================
            if (obj instanceof Collection<?> col) {
                println(indent, "[");
                for (Object item : col) {
                    print(item, indent + 2); // 🔥 aqui garante recursão correta
                }
                println(indent, "]");
                return;
            }

            // =========================
            // MAP
            // =========================
            if (obj instanceof Map<?, ?> map) {
                println(indent, "{");
                map.forEach((k, v) -> {
                    println(indent + 2, color(k.toString(), FIELD) + ":");
                    print(v, indent + 4);
                });
                println(indent, "}");
                return;
            }

            // =========================
            // OBJETOS DO JDK (fallback)
            // =========================
            if (clazz.getPackageName().startsWith("java.")) {
                println(indent, color(obj.toString(), VALUE));
                return;
            }

            // =========================
            // OBJETO NORMAL
            // =========================
            println(indent, color(clazz.getSimpleName(), TYPE) + " {");

            for (Field field : clazz.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);

                    println(indent + 2, color(field.getName(), FIELD) + ":");
                    print(value, indent + 4);

                } catch (Exception e) {
                    println(indent + 2, color(field.getName() + ": <erro>", FIELD));
                }
            }

            println(indent, "}");
        }

        // =========================
        // HELPERS
        // =========================

        private static boolean isPrimitiveLike(Object obj) {
            return obj instanceof String ||
                obj instanceof Number ||
                obj instanceof Boolean ||
                obj.getClass().isPrimitive();
        }

        private static void println(int indent, String text) {
            System.out.println(" ".repeat(indent) + text);
        }

        private static String color(String text, String color) {
            return color + text + RESET;
        }
    }
}
