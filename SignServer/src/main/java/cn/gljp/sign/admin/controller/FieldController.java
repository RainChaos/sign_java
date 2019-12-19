package cn.gljp.sign.admin.controller;

import cn.gljp.sign.admin.entity.*;
import cn.gljp.sign.admin.service.FieldService;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.gljp.sign.utils.TreeUtils;
import cn.hutool.json.JSONUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class FieldController {
    @Autowired
    private FieldService fieldService;
    @Autowired
    private HttpServletRequest request;

    /*--------学生字段管理--------------------------------------------------------*/
    @RequestMapping("/field/student/getStudentFieldListByKey")
    public Result getStudentFieldListByKey(String keyword) {
        List<Field> list=fieldService.getStudentFieldListByKey( keyword);
        List<FieldCustom> fieldCustomList=new ArrayList<>();
        for(Field f:list){
            FieldCustom fieldCustom=new FieldCustom();
            BeanUtils.copyProperties(f,fieldCustom);
            List<String> s=new ArrayList<>();
            s.add(f.getFartherName());
            s.add(f.getDictionaryType());
            fieldCustom.setDictionaryType(s);
            fieldCustomList.add(fieldCustom);
        }
        return new Result(StatusCode.OK, true,"",fieldCustomList);
    }
    @RequestMapping("/field/student/getStudentFieldListBySearch")
    public Result getStudentFieldListBySearch( Field field) {
        request.setAttribute("params", JSONUtil.toJsonStr(field));
        List<Field> list=fieldService.getStudentFieldListBySearch(field);
        return new Result(StatusCode.OK, true,"", list);
    }
    @RequestMapping("/field/student/insert")
    public Result insert(@RequestBody Field field) {
        request.setAttribute("params", JSONUtil.toJsonStr(field));
        try {
            fieldService.insert(field);
            fieldService.addColumn(field);
            return new Result(StatusCode.OK, true,"添加成功","添加成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "添加失败","添加失败");
        }
    }
    @RequestMapping("/field/student/update")
    public Result update(@RequestBody Field field) {
        request.setAttribute("params", JSONUtil.toJsonStr(field));
        try {
            fieldService.update(field);
            return new Result(StatusCode.OK, true,"修改成功","修改成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "修改失败","修改失败");
        }
    }
    @RequestMapping("/field/student/delete")
    public Result delete(@RequestBody Field field) {
        request.setAttribute("params", JSONUtil.toJsonStr(field));
        try {
            fieldService.delete(field);
            return new Result(StatusCode.OK, true,"删除成功","删除成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "删除失败","删除失败");
        }
    }
  /*--------表单字段管理--------------------------------------------------------*/
    @RequestMapping("/field/form/getFormFieldBySearch")
    public Result getFormFieldBySearch(FormField formField) {
        request.setAttribute("params", JSONUtil.toJsonStr(formField));
        List<FormField> list=fieldService.getFormFieldBySearch(formField);
        return new Result(StatusCode.OK, true,"", list);
    }
    @RequestMapping("/field/form/getFieldNameByFormName")
    public Result getFieldNameByFormName(FormField formField) {
        request.setAttribute("params", JSONUtil.toJsonStr(formField));
        List<FormField> list=fieldService.getFieldNameByFormName(formField);
        List<String> fieldNameList=new ArrayList();
        for (FormField f:list){
            String fieldName=f.getFieldName();
            fieldNameList.add(fieldName);
        }
        return new Result(StatusCode.OK, true,"", fieldNameList);
    }
    @RequestMapping("/field/form/insert")
    public Result formInsert(@RequestBody FormField field) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(field));
//        try {
            List<Field> f= fieldService.getStudentFieldByFieldName(field.getFieldName());
            if (f.size()==1){
                field.setDescribe(f.get(0).getDescribe());
            }else {
                return new Result(StatusCode.ERROR, false, "添加失败,英文标识不存在或者存在重复值","添加失败,英文标识不存在或者存在重复值");
            }
            field.setWidth(400);
            field.setLabelWidth(150);
            field.setSort(1);
            field.setIsDisabed(false);
            field.setIsMustInput(false);
            field.setIsShow(true);
            field.setIsExport(true);
            fieldService.fieldIsExistInTable(field);
            fieldService.formInsert(field);
            return new Result(StatusCode.OK, true,"添加成功","添加成功");
//        }catch (Exception e){
//            return new Result(StatusCode.ERROR, false, "添加失败","添加失败");
//        }
    }
    @RequestMapping("/field/form/update")
    public Result formUpdate(@RequestBody FormField field) {
        request.setAttribute("params", JSONUtil.toJsonStr(field));
        try {
            fieldService.formUpdate(field);
            return new Result(StatusCode.OK, true,"修改成功","修改成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "修改失败","修改失败");
        }
    }
    @RequestMapping("/field/form/delete")
    public Result formDelete(@RequestBody FormField field) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(field));
        if(field.getFieldName().equals("id")){
            throw new Exception("错误：id字段不可删除");
        }
        try {
            fieldService.formDelete(field);
            return new Result(StatusCode.OK, true,"删除成功","删除成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "删除失败","删除失败");
        }
    }
    @RequestMapping("/field/form/updateStudentFormFieldWidth")
    public Result updateStudentFormFieldWidth(@RequestBody FormField field) {
        request.setAttribute("params", JSONUtil.toJsonStr(field));
        try {
            fieldService.updateStudentFormFieldWidth(field);
            return new Result(StatusCode.OK, true,"修改成功","修改成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "修改失败","修改失败");
        }
    }@RequestMapping("/field/form/updateStudentFormFieldWidthV2")
    public Result updateStudentFormFieldWidthV2(@RequestBody FormField field) {
        request.setAttribute("params", JSONUtil.toJsonStr(field));
        try {
            fieldService.updateStudentFormFieldWidthV2(field);
            return new Result(StatusCode.OK, true,"修改成功","修改成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "修改失败","修改失败");
        }
    }
    @RequestMapping("/field/form/setOrderSortAll")
    public Result setOrderSortAll(FormField field) {
        request.setAttribute("params", JSONUtil.toJsonStr(field));
        try {
            fieldService.setOrderSortAll(field);
            return new Result(StatusCode.OK, true,"调整成功","调整成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "调整失败","调整失败");
        }
    }
    /*------表格字段管理---------------------------------------------------------------------*/
    @RequestMapping("/field/grid/getStudentSearchFieldList")
    public Result getStudentSearchFieldList(SearchField searchField) {
        request.setAttribute("params", JSONUtil.toJsonStr(searchField));
        List<SearchField> list=fieldService.getStudentSearchFieldList(searchField);
        return new Result(StatusCode.OK, true,"", list);
    }
    @RequestMapping("/field/grid/getGridSearchFieldList")
    public Result getGridSearchFieldListBySearch(GridSearchField gridSearchField) {
        request.setAttribute("params", JSONUtil.toJsonStr(gridSearchField));
        List<GridSearchField> list=fieldService.getGridSearchFieldListBySearch(gridSearchField);
        return new Result(StatusCode.OK, true,"", list);
    }
    @RequestMapping("/field/grid/getFieldNameByTableName")
    public Result getFieldNameByTableName(GridSearchField gridSearchField) {
        request.setAttribute("params", JSONUtil.toJsonStr(gridSearchField));
        List<GridSearchField> list=fieldService.getFieldNameByTableName( gridSearchField);
        List<String> fieldNameList=new ArrayList();
        for (GridSearchField f:list){
            String fieldName=f.getFieldName();
            fieldNameList.add(fieldName);
        }
        return new Result(StatusCode.OK, true,"", fieldNameList);
    }
    @RequestMapping("/field/grid/insert")
    public Result gridInsert(@RequestBody GridSearchField gridSearchField) {
        request.setAttribute("params", JSONUtil.toJsonStr(gridSearchField));
        try {
            gridSearchField.setSort(1);
            fieldService.gridInsert(gridSearchField);
            return new Result(StatusCode.OK, true,"添加成功","添加成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "添加失败","添加失败");
        }
    }
    @RequestMapping("/field/grid/update")
    public Result gridUpdate(@RequestBody GridSearchField gridSearchField) {
        request.setAttribute("params", JSONUtil.toJsonStr(gridSearchField));
        try {
            fieldService.gridUpdate(gridSearchField);
            return new Result(StatusCode.OK, true,"修改成功","修改成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "修改失败","修改失败");
        }
    }
    @RequestMapping("/field/grid/delete")
    public Result gridDelete(@RequestBody GridSearchField gridSearchField) {
        request.setAttribute("params", JSONUtil.toJsonStr(gridSearchField));
        try {
            fieldService.gridDelete(gridSearchField);
            return new Result(StatusCode.OK, true,"删除成功","删除成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "删除失败","删除失败");
        }
    }
    @RequestMapping("/field/grid/searchInsert")
    public Result searchInsert(@RequestBody SearchField searchField) {
        request.setAttribute("params", JSONUtil.toJsonStr(searchField));
        try {
            fieldService.searchInsert(searchField);
            return new Result(StatusCode.OK, true,"添加成功","添加成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "添加失败","添加失败");
        }
    }
    @RequestMapping("/field/grid/searchUpdate")
    public Result searchUpdate(@RequestBody SearchField searchField) {
        request.setAttribute("params", JSONUtil.toJsonStr(searchField));
        try {
            fieldService.searchUpdate(searchField);
            return new Result(StatusCode.OK, true,"修改成功","修改成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "修改失败","修改失败");
        }
    }
    @RequestMapping("/field/grid/searchDelete")
    public Result searchDelete(@RequestBody SearchField searchField) {
        request.setAttribute("params", JSONUtil.toJsonStr(searchField));
        try {
            fieldService.searchDelete(searchField);
            return new Result(StatusCode.OK, true,"删除成功","删除成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "删除失败","删除失败");
        }
    }

}
