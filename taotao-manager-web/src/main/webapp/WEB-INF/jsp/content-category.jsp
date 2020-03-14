<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <ul id="contentCategory" class="easyui-tree">  </ul>
</div>
<div id="contentCategoryMenu" class="easyui-menu" style="width:120px;" data-options="onClick:menuHandler">
    <div data-options="iconCls:'icon-add',name:'add'">添加</div>
    <div data-options="iconCls:'icon-remove',name:'rename'">重命名</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'icon-remove',name:'delete'">删除</div>
</div>
<script type="text/javascript">
$(function(){
	$("#contentCategory").tree({
		url : '/content/category/list',		//初始化树形视图
		animate: true,
		method : "GET",
		/* onContextMenu监听事件 */
		/*  e是事件对象  node是对应单击右键的节点*/
		onContextMenu: function(e,node){
            e.preventDefault();
            			/*把当前节点变成选择状态  */
            $(this).tree('select',node.target);
            							/* 菜单显示的坐标  */
            $('#contentCategoryMenu').menu('show',{
                left: e.pageX,
                top: e.pageY
            });
        },	//创新新节点对象后,鼠标离开后触发事件(编辑完成后会触发事件)
        onAfterEdit : function(node){
        	var _tree = $(this);
        	if(node.id == 0){//判断是否为新节点 (新节点id为0)
        		// 新增节点 写入后台
        		$.post("/content/category/create",{parentId:node.parentId,name:node.text},function(data){
        			if(data.status == 200){
        				_tree.tree("update",{
            				target : node.target,
            				id : data.data.id  //更新id id不为0了
            			});
        			}else{
        				$.messager.alert('提示','创建'+node.text+' 分类失败!');
        			}
        		});
        	}else{
        		//从命名后触发 即 id不为0时  更新数据库
        		$.post("/content/category/update",{id:node.id,name:node.text});
        	}
        }
	});
});

/* 点击菜单后,触发事件  参数:具体的项*/
function menuHandler(item){
	var tree = $("#contentCategory");
	//取得当前选中节点
	var node = tree.tree("getSelected");
	if(item.name === "add"){  //当点增加触发事件
		tree.tree('append', {//添加新的节点
            parent: (node?node.target:null),
            data: [{  //新增节点的对象 3个属性EasyUITreeNode对象匹配
                text: '新建分类',
                id : 0,
                parentId : node.id
            }]
        }); 
		var _node = tree.tree('find',0);//根节点 新增的节点id都为0
		tree.tree("select",_node.target).tree('beginEdit',_node.target);	//变成可编辑状态
	}else if(item.name === "rename"){
		tree.tree('beginEdit',node.target);
	}else if(item.name === "delete"){
		$.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){
			if(r){
				$.post("/content/category/delete/",{id:node.id},function(){
					tree.tree("remove",node.target);
				});	
			}
		});
	}
}
</script>