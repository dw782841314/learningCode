$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/author/list',
        datatype: "json",
        colModel: [			
			/*{ label: 'id', name: 'id', index: 'id', width: 50, key: true },*/
			{ label: '投稿人姓名', name: 'authorName', index: 'author_name', width: 80 }, 			
			{ label: '邮件', name: 'email', index: 'email', width: 80 }, 			
			{ label: '电话', name: 'mobile', index: 'mobile', width: 80 }, 			
			{ label: '部门id', name: 'deptId', index: 'dept_id', width: 80 }, 			
			{ label: '银行卡号', name: 'bankCard', index: 'bank_card', width: 80 }, 			
			{ label: '联系地址', name: 'addr', index: 'addr', width: 80 }, 			
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

// _search=false&nd=1551251878102&limit=10&page=1&sidx=&order=asc&_=1551251877845

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		authorInfo: {},
		searchName: ''
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.authorInfo = {};
		},
		search: function () {
			console.log(vm.searchName);
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    'searchName': vm.searchName
                },
                page:page
            }).trigger("reloadGrid");
        },
		update: function (event) {
			var id = getSelectedRow();
			console.log(id);
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			console.log(vm.authorInfo.id);
			var url = vm.authorInfo.id == null ? "sys/author/save" : "sys/author/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.authorInfo),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			console.log(ids);
			if(ids == null){
				return ;
			}
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "sys/author/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(id){
			$.get(baseURL + "sys/author/info/"+id, function(r){
                vm.authorInfo = r.authorInfo;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});