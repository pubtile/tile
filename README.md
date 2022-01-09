# tile

## 目标

一个企业级的开源框架，通过提升对象的描述能力，来摆脱面向过程的开发的困境，从而大幅提升开发效率和代码质量。



## 思路

+ 通过业务分析，在你的业务模型类上通过annotation的方式进行描述，确定一些基本的业务规则。
+ 开发人员根据特定需求，构建请求体，然后集成到该框架中，该框架就会根据模型上的定义来猜测出具体要进行的操作，然后执行他。

比较拗口，下面结合范例来理解下



## Sample

这里有个案例， 我们正在开发有个库存服务。



### 分析对象模型

然后我们看看模型本身。这个框架对于模型中的各个属性定位为两类，维度属性和值属性

+ 维度属性：

  维度属性特点时用于逻辑上identify某个对象的，类似于数据库中的逻辑主键。

  一个模型可以包含1个或者多个维度

  维度是有优先级的，当多个维度在请求中同时出现时，以高优先级的维度作为identify使用。

  每个维度包含1个和多个属性。

  每个维度有个名字(name)。

  维度有个dimensionMandatory的属性来描述该维度是否为必须存在，如果非否，如果该维度可以为空，这个多用于混合管理中使用。

  

+ 值属性

  不能标识一个对象，而只是起到描述作用。例如name,  description等等

  如果有个特别的值属性counter，含义是该模型是个可计数的资源模型。这样就具备了increase, decrease, 根据数量进行属性调整等等行为。



sample例子

```java
@Scope("prototype")
public class InvForTesting  extends BaseModel<InvForTesting, InvForTestingPo> {

    /**
     * 库存编号
     */
    @DimensionProperty(name = "inventoryNo", autoGenerate = true, generatorBeanName = "inventoryNoGenerator", priority = 50)
    private String inventoryNo;

    /**
     * 商品编码
     */
    @DimensionProperty(name = "inventoryDimension", priority = 500)
    private String skuId;

    /**
     * 库位编码
     */
    @DimensionProperty(name = "inventoryDimension")
    private String locationCode;

    /**
     * 容器编码 实物容器或虚拟容器
     */
    @DimensionProperty(name = "inventoryDimension", bankable = true)
    private String containerCode;

    /**
     * 货主编码
     */
    @DimensionProperty(name = "inventoryDimension",immutable = true)
    private String ownerCode;

//    @DimensionProperty(name = "inventoryDimension")
    @ValueProperty()
    private String vendorCode;

    /**
     * 唯一码
     */
    @DimensionProperty(name = "inventoryDimension", bankable = true)
    @DimensionProperty(name = "uniqueCode", bankable = false, dimensionMandatory = false,priority = 80)
    private String uniqueCode;

    /**
     * 现有数量。可用数量=现有数量+移入数量-分配数量-冻结数量
     */
    @ValueProperty(counter = true)
    private Integer onHandQty;

    /**
     * 分配数量
     */
    @ValueProperty(counter = true)
    private Integer allocatedQty;

    /**
     * 在途数量
     */
    @ValueProperty(counter = true)
    private Integer inTransitQty;


    /**
     * 冻结数量
     */
    @ValueProperty(counter = true)
    private Integer suspenseQty;

    /**
     * 入库单据类型，因为不同单据类型编号不相同，类型就不需要了
     */
    @ValueProperty
    private String entryOrderType;

}
```



BaseModel

```java
public abstract class BaseModel<M extends Model, P extends BasePo<P>> implements Model<M,P> {
    @ApiModelProperty(value = "主键")
    @DimensionProperty(name = "id",immutable = true, autoGenerate = true, priority = 10)
    private Long id;

    @ApiModelProperty(value = "租户id")
    @DimensionProperty(excludeNames = {"id"})
    private Long tenantId;

    /**
     * 表示该行数据是哪个系统用户创建的。这里特指系统用户，不包含顾客
     */
    @ValueProperty
    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    @ValueProperty(autoGenerate = true)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "最后更新人")
    @ValueProperty()
    private Long updatedBy;

    @ApiModelProperty(value = "最后更新时间")
    @ValueProperty(autoGenerate = true)
    private LocalDateTime updatedTime;

    @ValueProperty(autoGenerate = true)
    @ApiModelProperty(value = "版本号")
 
 	...
 }
```

从这个例子可以看到，InvForTesting有4个维度，优先级从高到低分别是id, inventoryNo, uniqueCode,inventoryDimension。

+ id就是主键了，这个不必多说。
+ inventoryNo 这个维度是inventoryNo+tenantId组合而成。含义是系统会为每个库存的自动生成个可以阅读的库存编号。为什么需要tenantId？因为我们是个SaaS系统。是支持多租户的。
+ uniqueCode 是uniqueCode+tenantId组合而成。逻辑含义是唯一吗管理的货品需要使用uniqueCode来区别各个库存。
+ inventoryDimension是由skuid, locationCode(库位编码)，containerCode（容器编码），ownerCode（货主编码）等等属性组成的的。逻辑上的含义是，当两个库存如果这些属性都相同，系统就认为他们是一样的。否则需要分开管理和控制。

除了维度这里还有多个**数量的属性，注意到用@ValueProperty(counter = true)来标识。含义是该模型是个可计数的资源模型。这样就具备了increase, decrease, 根据数量进行属性调整等等行为。



### 分析服务

库存服务的本身我们有如下核心API

+ 增加库存

  在收货时，增加库存，

+ 减少库存

  在发货时，减少库存。

+ 库存移动

  移库，把库存从一个位置移动到另一个位置，

+ 库存属性调整

  譬如质量等级，批号等进行调整



这个框架有个概念叫做指令，目前的指令包括，instructionInsert, instructionUpdate, instructionDelete, instructionIncrease等等。它是在传统的CRUD 和你的复杂业务服务之间。提供比传统的CRUD这样原始的持久化操作更复杂的系统处理能力。

比如instructionUpdate() 传入的参数包括 {id: 12, locationCode: 'A01', onHandQty: 3 } ,这个时候系统会解读为，你是希望把id的12的库存中的3件的locationCode改为A01

原先

| id   | locationCode | onHandQty |
| ---- | ------------ | --------- |
| 12   | A03          | 8         |

后来

| id   | locationCode | onHandQty |
| ---- | ------------ | --------- |
| 12   | A03          | 5         |
| 92   | A01          | 3         |



### 核心价值

从上面这个例子里，就提现了该框架的核心价值。这么复杂的业务处理，你只需要传入适当的参数，然后调用适当的instruction。就完成了。

