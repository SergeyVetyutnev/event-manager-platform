package dev.vetyutnev.eventmanagerplatform.common.utils;

import dev.vetyutnev.eventmanagerplatform.common.kafka.ChangeItem;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DiffUtils {

    public static <T> List<ChangeItem> generateChanges(T oldObj, T newObj){
        List<ChangeItem> changes = new ArrayList<>();

        if (oldObj == null || newObj == null){
            return changes;
        }

        Field[] fields = oldObj.getClass().getDeclaredFields();

        for (Field field : fields){
            field.setAccessible(true);
            try {
                Object oldValue = field.get(oldObj);
                Object newValue = field.get(newObj);

                if (!oldValue.equals(newValue)){
                    String oldStr = oldValue != null ? oldValue.toString() : null;
                    String newStr = newValue != null ? newValue.toString() : null;

                    changes.add(new ChangeItem(field.getName(), oldStr, newStr));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Ошибка при генерации difference", e);
            }
        }

        return changes;
    }
}
