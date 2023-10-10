package com.bjh.controller;


import com.bjh.entity.*;
import com.bjh.page.Page;
import com.bjh.service.*;
import com.bjh.utils.TokenUtils;
import com.bjh.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private StoreService storeService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductTypeService productTypeService;

    //注入SupplyService
    @Autowired
    private SupplyService supplyService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private TokenUtils tokenUtils;
    /*
    * 查询所有仓库的方法
    *
    * */
    @RequestMapping("/store-list")
    public Result storeList(){
        //执行业务
        List<Store> storeList = storeService.findAllStore();

        return Result.ok(storeList);

    }


    /*
    * 查询所有品牌的方法
    * */
    @RequestMapping("/brand-list")
    public Result brandList(){
        //执行业务
        List<Brand> brandList = brandService.queryAllBrand();
        //响应
        return Result.ok(brandList);
    }


    /*
    *   //分页查询商品的业务接口
    * */
    @RequestMapping("/product-page-list")
    public Result productListPage(Page page, Product product){
        page = productService.queryProductPage(page, product);

        return Result.ok(page);


    }

    //查询所有商品分类树的接口
    @RequestMapping("/category-tree")
    public Result loadTypeTree(){
        List<ProductType> typeTree = productTypeService.queryAllTypeTree();

        return Result.ok(typeTree);
    }



   /*
   * /**
    * 查询所有供应商的url接口/product/supply-list
    *
    * 返回值Result对象给客户端响应查询到的List<Supply>;
    */

    @RequestMapping("/supply-list")
    public Result supplyList(){
        //执行业务
        List<Supply> supplyList = supplyService.queryAllSupply();
        //响应
        return Result.ok(supplyList);
    }

    /*
     * 查询所有产地的url接口/product/place-list
     *
     * 返回值Result对象给客户端响应查询到的List<Place>;
     */
    @RequestMapping("/place-list")
    public Result placeList(){
        //执行业务
        List<Place> placeList = placeService.queryAllPlace();
        //响应
        return Result.ok(placeList);
    }


    /**
     * 查询所有单位的url接口/product/unit-list
     *
     * 返回值Result对象给客户端响应查询到的List<Unit>;
     */
    @RequestMapping("/unit-list")
    public Result unitList(){
        //执行业务
        List<Unit> unitList = unitService.queryAllUnit();
        //响应
        return Result.ok(unitList);
    }


    /*
    * 上传图片接口
    *
    * */
    @Value("${file.upload-path}")
    private String uploadPath;
    @CrossOrigin
    @RequestMapping("/img-upload")
    public Result uploadImg(MultipartFile file){

        try {
            //拿到上传图片的目录路径file对象
            File uploadDirFile = ResourceUtils.getFile(uploadPath);
            //磁盘路径
            String absolutePath = uploadDirFile.getAbsolutePath();
            //拿到上传图片的名称
            String filename = file.getOriginalFilename();
            //拿到上传的文件要保存的磁盘文件路径
            String uploadPath = absolutePath+"\\"+filename;
            //上传图片
            file.transferTo(new File(uploadPath));

            //成功响应
            return Result.ok("图片上传成功！");
        } catch (IOException e) {

            return Result.err(Result.CODE_ERR_BUSINESS,"图片上传失败！");
        }
    }

    /**
     * 添加商品的url接口/product/product-add
     *
     * @RequestBody Product product将添加的商品信息的json串数据封装到参数Product对象;
     * @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token
     * 将请求头Token的值即客户端归还的token赋值给参数变量token;
     */
    @RequestMapping("/product-add")
    public Result addProduct(@RequestBody Product product,
                             @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){

        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id,即添加商品的用户id
        int createBy = currentUser.getUserId();
        product.setCreateBy(createBy);

        //执行业务
        Result result = productService.saveProduct(product);

        //响应
        return result;
    }




    /*
    *
    * 修改商品状态
    * */
    @RequestMapping("/state-change")
    public Result modifyProductState(@RequestBody Product product){

        Result result = productService.updateProductStateByPid(product);
        return result;

    }

    /*
    * 删除单个商品
    * product/product-delete/23
    **/

    @RequestMapping("/product-delete/{productId}")
    public Result removeProduct(@PathVariable Integer productId ){
        //执行业务
        Result result = productService.deleteProductByIds(Arrays.asList(productId));
        return result;

    }


    /*
    批量删除
    /product/product-list-delete
    *
    * */
    @RequestMapping("/product-list-delete")
    public Result removeProductByIds(@RequestBody List<Integer> productIdList){

        Result result = productService.deleteProductByIds(productIdList);
        return result;

    }

    /*
    *
    * 修改
    * */

    @RequestMapping("/product-update")
    public Result updateProduct(@RequestBody Product product,
                                @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){

        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id,即修改商品的用户id
        int updateBy = currentUser.getUserId();
        product.setUpdateBy(updateBy);

        //执行业务
        Result result = productService.updateProduct(product);

        //响应
        return result;
    }


    /*
     * 导出
     * */
    @RequestMapping("/exportTable")
    public Result exportTable(Page page,Product product){
        //分页查询仓库
        page = productService.queryProductPage(page,product);
        //拿到当前页数据
        List<?> resultList = page.getResultList();
        //响应
        return Result.ok(resultList);
    }
}
