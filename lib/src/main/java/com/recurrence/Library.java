package com.recurrence;

import com.recurrence.contract.Recurrence;
import com.recurrence.contract.domain.Coupon;
import com.recurrence.contract.domain.ListCoupon;
import com.recurrence.contract.domain.ListPlan;
import com.recurrence.contract.domain.Plan;
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

        RequestResponse<ListCoupon> pagBankPlan = Recurrence.PagBank.config(
                        PagBankEnvironment.SANDBOX,
                        TOKEN)
                .coupon().list(0, 10);

        ObjectPrinter.print(pagBankPlan.getBody(),2);
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

        public static void print(Object obj) {
            print(obj, 0);
        }

        private static void print(Object obj, int indent) {
            if (obj == null) {
                println(indent, "null");
                return;
            }

            Class<?> clazz = obj.getClass();

            // TIPOS SIMPLES
            if (clazz.isPrimitive() ||
                    obj instanceof String ||
                    obj instanceof Number ||
                    obj instanceof Boolean) {
                println(indent, obj.toString());
                return;
            }

            // CLASSES DO JDK -> NÃO TENTAR REFLECTION
            if (clazz.getPackageName().startsWith("java.")) {
                println(indent, obj.toString());
                return;
            }

            // COLLECTION
            if (obj instanceof Collection<?> col) {
                println(indent, "[");
                for (Object item : col) {
                    print(item, indent + 2);
                }
                println(indent, "]");
                return;
            }

            // MAP
            if (obj instanceof Map<?, ?> map) {
                println(indent, "{");
                map.forEach((k, v) -> {
                    println(indent + 2, k + ":");
                    print(v, indent + 4);
                });
                println(indent, "}");
                return;
            }

            // OBJETO NORMAL
            println(indent, clazz.getSimpleName() + " {");

            for (Field field : clazz.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    println(indent + 2, field.getName() + ":");
                    print(field.get(obj), indent + 4);
                } catch (Exception ignored) {}
            }

            println(indent, "}");
        }

        private static void println(int indent, String text) {
            System.out.println(" ".repeat(indent) + text);
        }
    }
}
