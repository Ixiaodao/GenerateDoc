package com.ixiaodao.hepler;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.java.stubs.index.JavaShortClassNameIndex;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocToken;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.util.text.DateFormatUtil;
import com.ixiaodao.model.CarTestVO;
import com.ixiaodao.utils.JavaTypeUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

// 处理 实体自关联，第二层自关联字段
public class PsiClassCarTestHelper {
    PsiClass psiClass;

    private Module myModule;

    private Map<Integer, Integer> levelMap = new HashMap<>();
    private int idNum = 0;


    protected PsiClassCarTestHelper(@NotNull PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    public static PsiClassCarTestHelper create(@NotNull PsiClass psiClass) {
        return new PsiClassCarTestHelper(psiClass);
    }

    @NotNull
    protected Project getProject() {
        return psiClass.getProject();
    }


    public String convertClassToCarTestJSON(Project project , boolean prettyFormat) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if(prettyFormat) gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create() ;
        List<CarTestVO> jsonMap = new ArrayList<>();

        if(psiClass != null){
            if (JavaTypeUtil.isJavaBaseType(psiClass)) {
                return null;
            }
            levelMap.put(0, 0);
            assembleClassToMap(jsonMap, idNum, 0, psiClass, project, 1);
        }

        return gson.toJson(jsonMap);
    }


    public List<CarTestVO> convertClassToCarTestList(Project project , boolean prettyFormat) {
        List<CarTestVO> jsonMap = new ArrayList<>();
        if(psiClass != null){
            if (JavaTypeUtil.isJavaBaseType(psiClass)) {
                return null;
            }
            levelMap.put(0, 0);
            assembleClassToMap(jsonMap, idNum, 0, psiClass, project, 1);
        }
        return jsonMap;
    }

    public void assembleClassToMap(List<CarTestVO> list, Integer idTemp, Integer parentId, PsiClass psiClass, Project project,int recursiveCount) {
        PsiField[] fields = psiClass.getFields();
        for (PsiField field : fields) {
            String fieldName = field.getName();
            if ("serialVersionUID".equals(fieldName)) {
                continue;
            }

            PsiType psiFieldType = field.getType();
            String typeName = psiFieldType.getPresentableText();
            PsiDocComment docComment = field.getDocComment();
            String desc = "";
            if (docComment != null) {
                PsiElement[] descriptionElements = docComment.getDescriptionElements();
                for (PsiElement descriptionElement : descriptionElements) {
                    if (descriptionElement instanceof PsiDocToken) {
                        desc = desc + descriptionElement.getText() + "    ";
                    }
                }
            }

//            common base type
            if (isJavaBaseType(typeName)) {
                CarTestVO carTestVO = new CarTestVO();
                carTestVO.setId(idNum + 1);
                carTestVO.setParentId(parentId);
                carTestVO.setLevel(levelMap.get(parentId) + 1);
                carTestVO.setIdentifier(fieldName);
                carTestVO.setName(desc);
                carTestVO.setDataType(typeName);
                list.add(carTestVO);
                idNum++;
                levelMap.put(carTestVO.getId(), carTestVO.getLevel());
                continue;
            }


            if (isListFieldType(psiFieldType)) {
                CarTestVO carTestVO = new CarTestVO();
                carTestVO.setId(idNum + 1);
                carTestVO.setParentId(parentId);
                carTestVO.setLevel(levelMap.get(parentId) + 1);
                carTestVO.setIdentifier(fieldName);
                carTestVO.setName(desc);
                carTestVO.setDataType("List:Object");
                list.add(carTestVO);
                idNum++;
                levelMap.put(carTestVO.getId(), carTestVO.getLevel());

                PsiType[] parameters = ((PsiClassReferenceType) psiFieldType).getParameters();
                if (parameters != null && parameters.length > 0) {
                    PsiType parameter = parameters[0];
                    if (recursiveCount <= 0 )  {
                        continue;
                    }
                    Object baseTypeDefaultValue = getJavaBaseTypeDefaultValue( parameter.getPresentableText() );
                    if (baseTypeDefaultValue != null) {
                        carTestVO.setDataType("List:" + parameter.getPresentableText());
                        continue;
                    }

                    // TODO TODO TODO .................
                    if (parameter instanceof PsiClassReferenceType) {
                        if (parameter.getPresentableText().contains("<")) {
                            continue;
                        }
                        PsiClass onePsiClassByClassName = findOnePsiClassByClassName(parameter.getCanonicalText(), project);
                        if (onePsiClassByClassName != null) {
                            assembleClassToMap(list,idNum + 1, idNum, onePsiClassByClassName, project,1);
                        }
                    }
                }
                continue;
            }

            PsiClass resolveClass = ((PsiClassReferenceType) psiFieldType).resolve();

            if (resolveClass != null) {

                if (JavaTypeUtil.isJavaBaseType(resolveClass)) {
                    continue;
                }

                if (Objects.equals(resolveClass.getQualifiedName(), psiClass.getQualifiedName())) {
                    if (recursiveCount > 0) {
                        assembleClassToMap(list, idNum + 1, idNum, resolveClass, project,0);
                    }
                } else {
                    CarTestVO carTestVO = new CarTestVO();
                    carTestVO.setId(idNum + 1);
                    carTestVO.setParentId(parentId);
                    carTestVO.setLevel(levelMap.get(parentId) + 1);
                    carTestVO.setIdentifier(fieldName);
                    carTestVO.setName(desc);
                    carTestVO.setDataType(typeName);
                    list.add(carTestVO);
                    idNum++;
                    levelMap.put(carTestVO.getId(), carTestVO.getLevel());

                    assembleClassToMap(list,idNum + 1, idNum, resolveClass, project,1);
                }
            }

        }
    }

    @Nullable
    public static Object getJavaBaseTypeDefaultValue(String paramType) {
        Object paramValue = null;
//        todo: using map later
        switch (paramType.toLowerCase()) {
            case "byte": paramValue = Byte.valueOf("1");break;
            case "char": paramValue = Character.valueOf('Z');break;
            case "character": paramValue = Character.valueOf('Z');break;
            case "boolean": paramValue = Boolean.TRUE;break;
            case "int": paramValue = Integer.valueOf(1);break;
            case "integer": paramValue = Integer.valueOf(1);break;
            case "double": paramValue = Double.valueOf(1);break;
            case "float": paramValue = Float.valueOf(1.0F);break;
            case "long": paramValue = Long.valueOf(1L);break;
            case "short": paramValue = Short.valueOf("1");break;
            case "bigdecimal": return BigDecimal.ONE;
            case "string": paramValue = "demoData";break;
            case "date": paramValue = DateFormatUtil.formatDateTime(new Date());break; // todo: format date
//            default: paramValue = paramType;
        }
        return paramValue;
    }

    @Nullable
    public static PsiClass findOnePsiClassByClassName(String qualifiedClassName, Project project) {
        if (StringUtils.isEmpty(qualifiedClassName)) {
            return null;
        }
        return JavaPsiFacade.getInstance(project).findClass(qualifiedClassName, GlobalSearchScope.allScope(project));
    }

    @Nullable
    protected PsiClass findOnePsiClassByClassName_deprecated(String className, Project project) {
        PsiClass psiClass = null;

        String shortClassName = className.substring(className.lastIndexOf(".") + 1, className.length());
        Collection<PsiClass> psiClassCollection = tryDetectPsiClassByShortClassName(project, shortClassName);
        if (psiClassCollection.size() == 0) {

            return null;
        }
        if (psiClassCollection.size() == 1) {
            psiClass = psiClassCollection.iterator().next();
        }

        if (psiClassCollection.size() > 1) {
            //找import中对应的class
//            psiClass = psiClassCollection.stream().filter(tempPsiClass -> tempPsiClass.getQualifiedName().equals(className)).findFirst().get();

            Optional<PsiClass> any = psiClassCollection.stream().filter(tempPsiClass -> tempPsiClass.getQualifiedName().equals(className)).findAny();

            if (any.isPresent()) {
                psiClass = any.get();
            }

        }
        return psiClass;
    }

    public Collection<PsiClass> tryDetectPsiClassByShortClassName(Project project, String shortClassName) {
        Collection<PsiClass> psiClassCollection = JavaShortClassNameIndex.getInstance().get(shortClassName, project, GlobalSearchScope.projectScope(project));

        if (psiClassCollection.size() > 0) {
            return psiClassCollection;
        }

        if(myModule != null) {
            psiClassCollection = JavaShortClassNameIndex.getInstance().get(shortClassName, project, GlobalSearchScope.allScope(project));
        }

        return psiClassCollection;
    }


    @Nullable
    public PsiClass findOnePsiClassByClassName2(String className, Project project) {
        PsiClass psiClass = null;

        String shortClassName = className.substring(className.lastIndexOf(".") + 1, className.length());

//        psiClass.getPrimaryConstructor().getText(); // (val id: Long, val content: String)
//        psiClass.getFqName(); // class fullQualifiedName :org.jetbrains.kotlin.demo.Greeting

        PsiClass[] psiClasses = tryDetectPsiClassByShortClassName2( shortClassName,project);
        if (psiClasses.length == 0) {

            return null;
        }
        if (psiClasses.length == 1) {
            psiClass = psiClasses[0];
            return psiClass;
        }

        if (psiClasses.length > 1) {
            Optional<PsiClass> any = Arrays.stream(psiClasses).filter(tempPsiClass -> tempPsiClass.getQualifiedName().equals(className)).findAny();
            if (any.isPresent()) {
                psiClass = any.get();
            }

            for (PsiClass aClass : psiClasses) {

            }


            //找import中对应的class
//            psiClass = psiClassCollection.stream().filter(tempKtClass -> tempPsiClass.getQualifiedName().equals(className)).findFirst().get();

            /*Optional<PsiClass> any = psiClassCollection.stream().filter(tempPsiClass -> tempPsiClass.getQualifiedName().equals(className)).findAny();

            if (any.isPresent()) {
                psiClass = any.get();
            }*/

           /* for (KtClassOrObject ktClassOrObject : ktClassOrObjects) {
//                ktClassOrObject.
            }*/

        }
        return psiClass;
    }
    public PsiClass[] tryDetectPsiClassByShortClassName2(String shortClassName,Project project) {

        PsiClass[] psiClasses = PsiShortNamesCache.getInstance(project).getClassesByName(shortClassName, GlobalSearchScope.allScope(project));// 所有的

        if (psiClasses != null && psiClasses.length > 0) {
            return psiClasses;
        }

        if(myModule != null) {
            psiClasses = PsiShortNamesCache.getInstance(project).getClassesByName(shortClassName, GlobalSearchScope.allScope(project));// 所有的
            if (psiClasses != null && psiClasses.length > 0) {
                return psiClasses;
            }
        }

        return new PsiClass[0];
    }


    public List<Map<String, Object>> assembleClassToCarTestMap(PsiClass psiClass, Project project, int level) {
        List<Map<String, Object>> list = new ArrayList<>();
        int recursiveCount = 1;

        PsiField[] fields = psiClass.getFields();
        int i = 1;
        for (PsiField field : fields) {
            String fieldName = field.getName();
            if ("serialVersionUID".equals(fieldName)) {
                continue;
            }
            Map<String, Object> map = new LinkedHashMap<>();
            PsiType psiFieldType = field.getType();

            String typeName = psiFieldType.getPresentableText();

            PsiDocComment docComment = field.getDocComment();
            String desc = null;
            if (docComment != null) {
                PsiElement[] descriptionElements = docComment.getDescriptionElements();
                if (descriptionElements.length == 3) {
                    desc = descriptionElements[1].getText();
                }
            }

            if (isJavaBaseType(typeName)) {
                map.put("id", i);
                map.put("parentId", 0);
                map.put("level", level);
                map.put("identifier", fieldName);
                map.put("name", desc);
                map.put("dataType", typeName);
                list.add(map);
                continue;
            }


            PsiClass resolveClass = ((PsiClassReferenceType) psiFieldType).resolve();

            if (resolveClass != null) {

                PsiField[] fields1 = resolveClass.getFields();


                continue;
            }


            if (isListFieldType(psiFieldType)) {

                map.put("id", i);
                map.put("parentId", 0);
                map.put("level", level);
                map.put("identifier", fieldName);
                map.put("name", desc);
                map.put("dataType", "List:Object");
                list.add(map);

                level++;

                PsiType[] parameters = ((PsiClassReferenceType) psiFieldType).getParameters();
                if (parameters != null && parameters.length > 0) {
                    PsiType parameter = parameters[0];
// 自关联
                    if (recursiveCount <= 0 )  {
                        continue;
                    }

                    if (parameter.getPresentableText().equals(psiClass.getName())) {
                        List<Map<String, Object>> mapList = assembleClassToCarTestMap(psiClass, project, level);

                        for (Map<String, Object> objectMap : mapList) {

                        }

                        continue;
                    }

                    Object baseTypeDefaultValue = getJavaBaseTypeDefaultValue( parameter.getPresentableText() );
                    if (baseTypeDefaultValue != null) {
                        List<Object> objects = new ArrayList<>();
                        objects.add(baseTypeDefaultValue);
                        map.put(fieldName, objects );
                        continue;
                    }

                    // TODO TODO TODO .................
                    if (parameter instanceof PsiClassReferenceType) {
                        if (parameter.getPresentableText().contains("<")) {
                            continue;
                        }
                        PsiClass onePsiClassByClassName = findOnePsiClassByClassName(parameter.getCanonicalText(), project);

                        List<Map<String, Object>> objectMap = assembleClassToCarTestMap(onePsiClassByClassName, project, level);
                        for (Map<String, Object> stringObjectMap : objectMap) {
                            stringObjectMap.put("parentId", i);
                            map.put("level", level);
                            list.add(stringObjectMap);
                        }
                        continue;
                    }
                }


            }
            i++;
            list.add(map);
        }

        return list;

    }

    /* 字段是否为List 类型*/
    private static boolean isListFieldType(PsiType psiFieldType) {
        if (! (psiFieldType instanceof PsiClassReferenceType)) {
            return false;
        }

        PsiClass resolvePsiClass = ((PsiClassReferenceType) psiFieldType).resolve();
        if (resolvePsiClass.getQualifiedName().equals("java.util.List")) {
            return true ;
        }

        /*if (resolvePsiClass.getSuperClass().getQualifiedName().equals("java.util.List")) {
            return true ;
        }*/

        for (PsiType psiType : ((PsiClassReferenceType) psiFieldType).rawType().getSuperTypes()) {
            if (psiType.getCanonicalText().equals("java.util.List")) {
                return true;
            }
        }

        return false;

/*        ((PsiClassReferenceType) psiFieldType).rawType().getCanonicalText().equals("java.util.List");

        resolvePsiClass.getInterfaces();*/

//        rawType().getSuperTypes()/*
    }

    /* 字段是否为List 类型*/
    private static boolean isEnum(PsiType psiFieldType) {
        if (! (psiFieldType instanceof PsiClassReferenceType)) {
            return false;
        }
        return ((PsiClassReferenceType) psiFieldType).resolve().isEnum();
    }



    public PsiClassCarTestHelper withModule(Module module) {
        this.myModule = module;
        return this;
    }

    private static boolean isJavaBaseType(String typeName) {
        return getJavaBaseTypeDefaultValue(typeName) != null;
    }

    public String getByType (String type) {
        return "Long";
    }
}