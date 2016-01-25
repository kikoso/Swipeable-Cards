/**
 * AndTinder v0.1 for Android
 *
 * @Author: Enrique López Mañas <eenriquelopez@gmail.com>
 * http://www.lopez-manas.com
 *
 * TAndTinder is a native library for Android that provide a
 * Tinder card like effect. A card can be constructed using an
 * image and displayed with animation effects, dismiss-to-like
 * and dismiss-to-unlike, and use different sorting mechanisms.
 *
 * AndTinder is compatible with API Level 13 and upwards
 *
 * @copyright: Enrique López Mañas
 * @license: Apache License 2.0
 */

package com.andtinder.model;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class PojoTest {

    private ArrayList<PojoClass> pojoClasses = new ArrayList<>();

    @Before
    public void setup() {
        String[] packages = {"com.andtinder","com.andtinder.model","com.andtinder.view"};
        for (String pojoPackage : packages) {
            List<PojoClass> packagePojoClasses = PojoClassFactory.getPojoClasses(pojoPackage, new FilterPackageInfo());
            for (PojoClass clazz : packagePojoClasses) {
                if (clazz.getName().contains("$") || clazz.isAbstract() || clazz.isInterface() || clazz.isEnum()
                        || clazz.getName().endsWith("Test")|| clazz.getName().endsWith("CardModel")
                        ||  clazz.getName().endsWith("CardContainer")
                        ||  clazz.getName().endsWith("SimpleCardStackAdapter"))
                    continue;
                pojoClasses.add(clazz);
            }
        }
    }

    @Test
    public void testGettersAuto() {
        Validator validator = ValidatorBuilder.create().with(new GetterTester()).build();
        for (PojoClass clazz : pojoClasses) {
            try {
                validator.validate(clazz);
            } catch (AssertionError ex) {
                continue;
            }
        }
    }
}
