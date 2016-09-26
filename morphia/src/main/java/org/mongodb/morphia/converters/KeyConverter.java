package org.mongodb.morphia.converters;

import com.mongodb.DBRef;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.mapping.MappedField;

/**
 * @author Uwe Schaefer, (us@thomas-daily.de)
 * @author scotthernandez
 */
public class KeyConverter extends TypeConverter {

    /**
     * Creates the Converter.
     */
    public KeyConverter() {
        super(Key.class);
    }

    @Override
    public Object decode(final Class targetClass, final Object o, final MappedField optionalExtraInfo) {
        if (o == null) {
            return null;
        }
        if (!(o instanceof DBRef)) {
            throw new ConverterException(String.format("cannot convert %s to Key because it isn't a DBRef", o.toString()));
        }


        DBRef ref = (DBRef) o;
        Class<?> keyType;
        if (optionalExtraInfo != null && optionalExtraInfo.getTypeParameters().size() == 1) {
            keyType = optionalExtraInfo.getTypeParameters().get(0).getType();
        } else {
            keyType = getMapper().getClassFromCollection(ref.getCollectionName());
        }
        final Key<?> key = new Key<Object>(keyType, ref.getCollectionName(), ref.getId());

        return key;
    }

    @Override
    public Object encode(final Object t, final MappedField optionalExtraInfo) {
        if (t == null) {
            return null;
        }
        return getMapper().keyToDBRef((Key) t);
    }

}
