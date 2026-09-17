// 拦截全局fetch请求
    const originalFetch = window.fetch;
    window.fetch = async function (url, options = {}) {
        options.headers = options.headers || {};
        const token = localStorage.getItem('token');
        if (token) {
            options.headers['token'] = token;
        }
        const response = await originalFetch(url, options);
        if (response.headers.get('content-type')?.includes('application/json')) {
            const clone = response.clone();
            try {
                const json = await clone.json();
                if ([203, 204, 205, 206, 207].includes(json.code)) {
                    localStorage.removeItem('token');
                    window.location.href = '/login';
                }
            } catch (e) {
            }
        }
        return response;
    };

    // 检查本地是否存在token
    if (!localStorage.getItem('token')) {
        // 重定向到登录页面
        window.location.href = '/login';
    }

    // 解析JWT获取当前登录用户信息
    function getCurrentUser() {
        const token = localStorage.getItem('token');
        if (!token) return null;
        try {
            const parts = token.split('.');
            if (parts.length !== 3) return null;
            const payloadBase64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
            const jsonStr = decodeURIComponent(atob(payloadBase64).split('').map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join(''));
            const payload = JSON.parse(jsonStr);
            return payload.user;
        } catch (e) {
            console.error('解析 token 失败', e);
            return null;
        }
    }

    // 更新头部登录人信息
    function updateLoginStatus() {
        const user = getCurrentUser();
        const avatarDiv = document.getElementById('user-avatar');
        const dropAvatarDiv = document.getElementById('dropdown-avatar');
        const nameDiv = document.getElementById('dropdown-user-name');
        const usernameDiv = document.getElementById('dropdown-user-username');
        const roleSpan = document.getElementById('dropdown-user-role');

        if (user) {
            const displayName = user.name || user.username || 'User';
            const firstChar = displayName.charAt(0).toUpperCase();
            avatarDiv.textContent = firstChar;
            if (dropAvatarDiv) dropAvatarDiv.textContent = firstChar;
            nameDiv.textContent = displayName;
            usernameDiv.textContent = `@${user.username}`;

            const isAdmin = user.role === 'ADMIN' || user.role === '管理员';
            roleSpan.textContent = isAdmin ? '管理员' : '普通用户';
            roleSpan.className = `status-badge ${isAdmin ? 'status-1' : 'status-0'}`;
        } else {
            avatarDiv.textContent = 'U';
            if (dropAvatarDiv) dropAvatarDiv.textContent = 'U';
            nameDiv.textContent = '未登录';
            usernameDiv.textContent = '@Guest';
            roleSpan.textContent = '-';
            roleSpan.className = 'status-badge';
        }
    }

    // 下拉菜单逻辑
    window.toggleProfileDropdown = function (event) {
        event.stopPropagation();
        const dropdown = document.getElementById('profile-dropdown');
        dropdown.classList.toggle('show');
    };

    document.addEventListener('click', (event) => {
        const dropdown = document.getElementById('profile-dropdown');
        const avatar = document.getElementById('user-avatar');
        if (dropdown && dropdown.classList.contains('show') && event.target !== avatar && !dropdown.contains(event.target)) {
            dropdown.classList.remove('show');
        }
    });

    // 退出登录
    async function logout() {
        if (!confirm('确定要退出登录吗？')) return;
        try {
            await fetch('/logout', {method: 'POST'});
        } catch (err) {
            console.error('退出请求失败', err);
        } finally {
            localStorage.removeItem('token');
            window.location.href = '/login';
        }
    }

    // 主题切换逻辑
    function toggleTheme() {
        const currentTheme = document.documentElement.getAttribute('data-theme');
        let targetTheme = 'dark';
        if (currentTheme !== 'light') {
            targetTheme = 'light';
        }

        document.documentElement.setAttribute('data-theme', targetTheme);
        localStorage.setItem('theme', targetTheme);

        updateThemeIcons(targetTheme);
    }

    function updateThemeIcons(theme) {
        const sunIcon = document.getElementById('sun-icon');
        const moonIcon = document.getElementById('moon-icon');
        if (theme === 'light') {
            sunIcon.style.display = 'none';
            moonIcon.style.display = 'block';
        } else {
            sunIcon.style.display = 'block';
            moonIcon.style.display = 'none';
        }
    }

    // 消息通知通知组件
    function showToast(title, desc, type = 'info') {
        const container = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;

        let iconText = 'i';
        if (type === 'success') iconText = '✓';
        if (type === 'error') iconText = '✗';

        toast.innerHTML = `
                <div class="toast-icon">${iconText}</div>
                <div class="toast-content">
                    <div class="toast-title">${title}</div>
                    <div class="toast-desc">${desc}</div>
                </div>
                <button class="toast-close" onclick="this.parentElement.remove()">&times;</button>
            `;
        container.appendChild(toast);

        // 触发进入动画
        setTimeout(() => toast.classList.add('show'), 50);

        // 5秒后自动销毁
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 400);
        }, 5000);
    }

    // 格式化文件大小
    function formatBytes(bytes) {
        if (bytes === 0) return '0 Bytes';
        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    }


    // 加载用户列表
    async function loadUsers() {
        const searchId = document.getElementById('search-id').value;
        const searchName = document.getElementById('search-name').value;
        const searchAge = document.getElementById('search-age').value;

        let queryParams = [];
        if (searchId) queryParams.push(`id=${searchId}`);
        if (searchName) queryParams.push(`name=${searchName}`);
        if (searchAge) queryParams.push(`age=${searchAge}`);

        const url = '/users' + (queryParams.length ? '?' + queryParams.join('&') : '');

        try {
            const response = await fetch(url);
            if (!response.ok) {
                showToast('加载用户失败', '网络响应错误', 'error');
                return;
            }
            const result = await response.json();

            if (result.code !== 200) {
                showToast('加载用户失败', result.msg || '加载用户列表失败', 'error');
                return;
            }

            const users = result.data['userList'] || [];

            const tbody = document.getElementById('user-table-body');
            tbody.innerHTML = '';

            if (users.length === 0) {
                tbody.innerHTML = `<tr><td colspan="9" style="text-align:center; color:var(--text-muted);">暂无用户数据</td></tr>`;
                return;
            }

            users.forEach(user => {
                const row = document.createElement('tr');

                let hobbiesHtml = '-';
                if (user.hobbies && user.hobbies.length > 0) {
                    hobbiesHtml = user.hobbies
                        .map(h => `<span class="tag-badge" style="margin: 2px; font-size: 0.75rem; padding: 0.15rem 0.4rem;">${h}</span>`)
                        .join('');
                }

                row.innerHTML = `
                        <td style="font-weight:600; color:var(--primary)">${user.id}</td>
                        <td>${user.name || '-'}</td>
                        <td>${user.age || '-'}</td>
                        <td>${user.sex || '-'}</td>
                        <td>${user.city || '-'}</td>
                        <td style="white-space: normal;"><div style="display: flex; flex-wrap: wrap; justify-content: center; gap: 4px; min-width: 120px; max-width: 250px;">${hobbiesHtml}</div></td>
                        <td style="max-width: 150px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" title="${user.description || ''}">${user.description || '-'}</td>
                        <td><span class="status-badge ${user.role === '管理员' ? 'status-1' : 'status-0'}">${user.role || '-'}</span></td>
                        <td>
                            <div class="btn-group" style="display: inline-flex; gap: 0.5rem;">
                                <button class="btn btn-primary btn-sm" onclick="openEditModal(${user.id})">编辑</button>
                                <button class="btn btn-danger btn-sm" onclick="deleteUser(${user.id})">删除</button>
                            </div>
                        </td>
                    `;
                tbody.appendChild(row);
            });
        } catch (err) {
            showToast('加载用户失败', err.message, 'error');
        }
    }

    // 删除用户
    async function deleteUser(id) {
        if (!confirm(`确定删除ID为 ${id} 的用户吗？`)) return;
        try {
            const response = await fetch(`/users/${id}`, {method: 'DELETE'});
            if (response.ok) {
                showToast('删除成功', `已删除ID为 ${id} 的用户`, 'success');
                await loadUsers();
            } else {
                showToast('操作失败', '删除失败', 'error');
            }
        } catch (err) {
            showToast('操作失败', err.message, 'error');
        }
    }

    // 省份与城市级联数据
    const locationData = {
        "北京市": ["北京市"],
        "上海市": ["上海市"],
        "天津市": ["天津市"],
        "重庆市": ["重庆市"],
        "河北省": ["石家庄市", "唐山市", "秦皇岛市", "邯郸市", "邢台市", "保定市", "张家口市", "承德市", "沧州市", "廊坊市", "衡水市"],
        "山西省": ["太原市", "大同市", "阳泉市", "长治市", "晋城市", "朔州市", "晋中市", "运城市", "忻州市", "临汾市", "吕梁市"],
        "内蒙古自治区": ["呼和浩特市", "包头市", "乌海市", "赤峰市", "通辽市", "鄂尔多斯市", "呼伦贝尔市", "巴彦淖尔市", "乌兰察布市", "兴安盟", "锡林郭勒盟", "阿拉善盟"],
        "辽宁省": ["沈阳市", "大连市", "鞍山市", "抚顺市", "本溪市", "丹东市", "锦州市", "营口市", "阜新市", "辽阳市", "盘锦市", "铁岭市", "朝阳市", "葫芦岛市"],
        "吉林省": ["长春市", "吉林市", "四平市", "辽源市", "通化市", "白山市", "松原市", "白城市", "延边朝鲜族自治州"],
        "黑龙江省": ["哈尔滨市", "齐齐哈尔市", "鸡西市", "鹤岗市", "双鸭山市", "大庆市", "伊春市", "佳木斯市", "七台河市", "牡丹江市", "黑河市", "绥化市", "大兴安岭地区"],
        "江苏省": ["南京市", "无锡市", "徐州市", "常州市", "苏州市", "南通市", "连云港市", "淮安市", "盐城市", "扬州市", "镇江市", "泰州市", "宿迁市"],
        "浙江省": ["杭州市", "宁波市", "温州市", "嘉兴市", "湖州市", "绍兴市", "金华市", "衢州市", "舟山市", "台州市", "丽水市"],
        "安徽省": ["合肥市", "芜湖市", "蚌埠市", "淮南市", "马鞍山市", "淮北市", "铜陵市", "安庆市", "黄山市", "滁州市", "阜阳市", "宿州市", "六安市", "亳州市", "池州市", "宣城市"],
        "福建省": ["福州市", "厦门市", "莆田市", "三明市", "泉州市", "漳州市", "南平市", "龙岩市", "宁德市"],
        "江西省": ["南昌市", "景镇市", "萍乡市", "九江市", "新余市", "鹰潭市", "赣州市", "吉安市", "宜春市", "抚州市", "上饶市"],
        "山东省": ["济南市", "青岛市", "淄博市", "枣庄市", "东营市", "烟台市", "潍坊市", "济宁市", "泰安市", "威海市", "日照市", "临沂市", "德州市", "聊城市", "滨州市", "菏泽市"],
        "河南省": ["郑州市", "开封市", "洛阳市", "平顶山市", "安阳市", "鹤壁市", "新乡市", "焦作市", "濮阳市", "许昌市", "漯河市", "三门峡市", "南阳市", "商丘市", "信阳市", "周口市", "驻马店市", "济源市"],
        "湖北省": ["武汉市", "黄石市", "十堰市", "宜昌市", "襄阳市", "鄂州市", "荆门市", "孝感市", "荆州市", "黄冈市", "咸宁市", "随州市", "恩施土家族苗族自治州", "仙桃市", "潜江市", "天门市", "神农架林区"],
        "湖南省": ["长沙市", "株洲市", "湘潭市", "衡阳市", "邵阳市", "岳阳市", "常德市", "张家界市", "益阳市", "郴州市", "永州市", "怀化市", "娄底市", "湘西土家族苗族自治州"],
        "广东省": ["广州市", "韶关市", "深圳市", "珠海市", "汕头市", "佛山市", "江门市", "湛江市", "茂名市", "肇庆市", "惠州市", "梅州市", "汕尾市", "河源市", "阳江市", "清远市", "东莞市", "中山市", "潮州市", "揭阳市", "云浮市"],
        "广西壮族自治区": ["南宁市", "柳州市", "桂林市", "梧州市", "北海市", "防城港市", "钦州市", "贵港市", "玉林市", "百色市", "贺州市", "河池市", "来宾市", "崇左市"],
        "海南省": ["海口市", "三亚市", "三沙市", "儋州市", "五指山市", "琼海市", "文昌市", "万宁市", "东方市", "定安县", "屯昌县", "澄迈县", "临高县", "白沙黎族自治县", "昌江黎族自治县", "乐东黎族自治县", "陵水黎族自治县", "保亭黎族苗族自治县", "琼中黎族苗族自治县"],
        "四川省": ["成都市", "自贡市", "攀枝花市", "泸州市", "德阳市", "绵阳市", "广元市", "遂宁市", "内江市", "乐山市", "南充市", "眉山市", "宜宾市", "广安市", "达州市", "雅安市", "巴中市", "资阳市", "阿坝藏族羌族自治州", "甘孜藏族自治州", "凉山彝族自治州"],
        "贵州省": ["贵阳市", "六盘水市", "遵义市", "安顺市", "铜仁市", "黔西南布依族苗族自治州", "毕节市", "黔东南苗族侗族自治州", "黔南布依族苗族自治州"],
        "云南省": ["昆明市", "曲靖市", "玉溪市", "保山市", "昭通市", "丽江市", "普洱市", "临沧市", "楚雄彝族自治州", "红河哈尼族彝族自治州", "文山壮族苗族自治州", "西双版纳傣族自治州", "大理白族自治州", "德宏傣族景颇族自治州", "怒江傈僳族自治州", "迪庆藏族自治州"],
        "西藏自治区": ["拉萨市", "日喀则市", "昌都市", "林芝市", "山南市", "那曲市", "阿里地区"],
        "陕西省": ["西安市", "铜川市", "宝鸡市", "咸阳市", "渭南市", "延安市", "汉中市", "榆林市", "安康市", "商洛市"],
        "甘肃省": ["兰州市", "嘉峪关市", "金昌市", "白银市", "天水市", "武威市", "张掖市", "平凉市", "酒泉市", "庆阳市", "定西市", "陇南市", "临夏回族自治州", "甘南藏族自治州"],
        "青海省": ["西宁市", "海东市", "海北藏族自治州", "黄南藏族自治州", "海南藏族自治州", "果洛藏族自治州", "玉树藏族自治州", "海西蒙古族藏族自治州"],
        "宁夏回族自治区": ["银川市", "石嘴山市", "吴忠市", "固原市", "中卫市"],
        "新疆维吾尔自治区": ["乌鲁木齐市", "克拉玛依市", "吐鲁番市", "哈密市", "昌吉回族自治州", "博尔塔拉蒙古自治州", "巴音郭楞蒙古自治州", "阿克苏地区", "克孜勒苏柯尔克孜自治州", "喀什地区", "和田地区", "伊犁哈萨克自治州", "塔城地区", "阿勒泰地区", "石河子市", "阿拉尔市", "图木舒克市", "五家渠市", "铁门关市"],
        "香港特别行政区": ["香港岛", "九龙", "新界"],
        "澳门特别行政区": ["澳门半岛", "氹仔岛", "路环岛"],
        "台湾省": ["台北市", "新北市", "桃园市", "台中市", "台南市", "高雄市", "基隆市", "新竹市", "嘉义市", "新竹县", "苗栗县", "彰化县", "南投县", "云林县", "嘉义县", "屏东县", "宜兰县", "花莲县", "台东县", "澎湖县", "金门县", "连江县"]
    };

    // 初始化省份下拉列表
    function initProvinceSelect() {
        const provinceSelect = document.getElementById('add-province');
        provinceSelect.innerHTML = '<option value="">请选择省份</option>';
        Object.keys(locationData).forEach(prov => {
            const opt = document.createElement('option');
            opt.value = prov;
            opt.textContent = prov;
            provinceSelect.appendChild(opt);
        });
        document.getElementById('add-city').innerHTML = '<option value="">请选择城市</option>';
    }

    // 省份切换联动城市
    function onProvinceChange() {
        const provinceVal = document.getElementById('add-province').value;
        const citySelect = document.getElementById('add-city');
        citySelect.innerHTML = '<option value="">请选择城市</option>';

        if (provinceVal && locationData[provinceVal]) {
            locationData[provinceVal].forEach(city => {
                const opt = document.createElement('option');
                opt.value = city;
                opt.textContent = city;
                citySelect.appendChild(opt);
            });
        }
    }

    // 打开/关闭模态框
    function openAddModal() {
        document.getElementById('modal-title').innerText = '添加新用户';
        document.getElementById('password-label').innerText = '密码 *';
        document.getElementById('add-password').required = true;
        document.getElementById('edit-user-id').value = '';
        document.getElementById('add-username').value = '';

        initProvinceSelect();
        tagsArray = [];
        renderTags();
        document.getElementById('add-user-modal').classList.add('show');
    }

    function closeAddModal() {
        document.getElementById('add-user-modal').classList.remove('show');
        document.getElementById('add-user-form').reset();
        document.getElementById('edit-user-id').value = '';
        document.getElementById('add-city').innerHTML = '<option value="">请选择城市</option>';
        tagsArray = [];
        renderTags();
    }

    // 打开编辑模态框
    async function openEditModal(id) {
        document.getElementById('modal-title').innerText = '编辑用户';
        document.getElementById('password-label').innerText = '密码 (留空不修改)';
        document.getElementById('add-password').required = false;
        document.getElementById('edit-user-id').value = id;

        initProvinceSelect();

        try {
            const response = await fetch(`/users/${id}`);
            const result = await response.json();
            if (result.code !== 200) {
                showToast('获取详情失败', result.msg || '获取用户详情失败', 'error');
                return;
            }
            const user = result.data['user'];
            if (!user) {
                showToast('获取详情失败', '用户不存在或已被删除', 'error');
                return;
            }

            document.getElementById('add-name').value = user.name || '';
            document.getElementById('add-username').value = user.username || '';
            document.getElementById('add-age').value = user.age || '';
            document.getElementById('add-sex').value = user.sex || '男';
            document.getElementById('add-role').value = user.role || '普通用户';

            if (user.province) {
                document.getElementById('add-province').value = user.province;
                onProvinceChange();
                if (user.city) {
                    document.getElementById('add-city').value = user.city;
                }
            }

            const hobbiesValue = (user.hobbies && user.hobbies.length) ? user.hobbies.join(',') : '';
            document.getElementById('add-hobbies').value = hobbiesValue;
            tagsArray = hobbiesValue ? hobbiesValue.split(',').map(s => s.trim()).filter(Boolean) : [];
            renderTags();

            document.getElementById('add-address').value = user.address || '';
            document.getElementById('add-description').value = user.description || '';

            document.getElementById('add-user-modal').classList.add('show');
        } catch (err) {
            showToast('获取详情失败', err.message, 'error');
        }
    }

    // 提交添加或修改用户
    async function submitAddUser(event) {
        event.preventDefault();
        const editId = document.getElementById('edit-user-id').value;
        const passwordVal = document.getElementById('add-password').value;
        const payload = {
            name: document.getElementById('add-name').value,
            username: document.getElementById('add-username').value.trim(),
            age: parseInt(document.getElementById('add-age').value) || null,
            sex: document.getElementById('add-sex').value,
            province: document.getElementById('add-province').value,
            city: document.getElementById('add-city').value || null,
            address: document.getElementById('add-address').value,
            hobbies: document.getElementById('add-hobbies').value ? document.getElementById('add-hobbies').value.split(',').map(s => s.trim()).filter(Boolean) : [],
            description: document.getElementById('add-description').value || null,
            role: document.getElementById('add-role').value
        };

        if (passwordVal) {
            payload.password = passwordVal;
        } else if (!editId) {
            payload.password = "";
        }

        try {
            const url = editId ? `/users/${editId}` : '/users';
            const method = editId ? 'PUT' : 'POST';
            const successTitle = editId ? '用户修改成功' : '用户创建成功';
            const successDesc = editId ? '已保存用户修改信息' : '已向数据库插入新用户记录';

            const response = await fetch(url, {
                method: method,
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                showToast(successTitle, successDesc, 'success');
                closeAddModal();
                await loadUsers();
            } else {
                const errMsg = await response.text();
                showToast('提交失败', errMsg || '提交失败', 'error');
            }
        } catch (err) {
            showToast('提交失败', err.message, 'error');
        }
    }

    function resetSearch() {
        document.getElementById('search-id').value = '';
        document.getElementById('search-name').value = '';
        document.getElementById('search-age').value = '';
        loadUsers();
    }


    // 上传单文件
    async function uploadSingleFile(file) {
        if (!file) return;
        const formData = new FormData();
        formData.append('file', file);

        showToast('开始上传', `正在上传单文件: ${file.name}...`, 'info');
        try {
            const response = await fetch('/files/upload', {
                method: 'POST',
                body: formData
            });
            const result = await response.json();
            if (result.code === 200) {
                showToast('上传成功', `文件 ${file.name} 已保存到服务器桌面！`, 'success');
            } else {
                showToast('文件上传失败', result.msg, 'error');
            }
        } catch (err) {
            showToast('文件上传失败', err.message, 'error');
        }
    }

    // 批量上传文件
    async function uploadBatchFiles(files) {
        if (!files || files.length === 0) return;
        const formData = new FormData();
        for (let file of files) {
            formData.append('files', file);
        }

        showToast('开始上传', `正在上传 ${files.length} 个文件...`, 'info');
        try {
            const response = await fetch('/files/batch-upload', {
                method: 'POST',
                body: formData
            });
            const result = await response.json();
            if (result.code === 200) {
                showToast('批量上传成功', result.msg, 'success');
            } else {
                showToast('批量上传失败', result.msg, 'error');
            }
        } catch (err) {
            showToast('批量上传失败', err.message, 'error');
        }
    }

    // 导入Excel
    async function importExcel() {
        const fileInput = document.getElementById('excel-import-file');
        const file = fileInput.files[0];
        if (!file) {
            showToast('未选择文件', '请先选择需要导入的 Excel/CSV 文件', 'error');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        showToast('导入中', '正在解析 Excel 文件并将用户保存至数据库...', 'info');
        try {
            const response = await fetch('/users/import', {
                method: 'POST',
                body: formData
            });
            const result = await response.json();
            if (result.code === 200) {
                showToast('导入成功', `成功导入并落库 ${result.data['userList'].length} 条用户数据`, 'success');
                fileInput.value = '';
                await loadUsers();
            } else {
                showToast('导入失败', result.msg, 'error');
            }
        } catch (err) {
            showToast('导入失败', err.message, 'error');
        }
    }

    // 获取选中的导出字段
    function getSelectedExportFields() {
        const checkboxes = document.querySelectorAll('input[name="export-fields"]:checked');
        return Array.from(checkboxes).map(cb => cb.value);
    }

    // 浏览器同步下载文件
    async function exportToBrowser() {
        const fieldList = getSelectedExportFields();
        if (fieldList.length === 0) {
            showToast('缺少参数', '请至少勾选一个导出字段', 'error');
            return;
        }

        showToast('开始生成', '正在下载 Excel 导出包...', 'info');
        try {
            const response = await fetch('/users/export', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({field_list: fieldList})
            });

            if (!response.ok) {
                showToast('导出失败', '导出下载失败', 'error');
                return;
            }

            // 从响应头解析动态文件名
            let fileName = '用户数据.xlsx';
            const disposition = response.headers.get('content-disposition');
            if (disposition) {
                const match = disposition.match(/filename\*?=(?:UTF-8'')?([^;]+)/i);
                if (match && match[1]) {
                    fileName = decodeURIComponent(match[1].replace(/["']/g, ''));
                }
            }

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = fileName;
            document.body.appendChild(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
            showToast('导出成功', `文件 ${fileName} 已经成功保存到您的本地`, 'success');
        } catch (err) {
            showToast('导出失败', err.message, 'error');
        }
    }

    // 后台异步导出到本地（仅传勾选字段列表）
    async function exportToLocal() {
        const fieldList = getSelectedExportFields();
        if (fieldList.length === 0) {
            showToast('缺少参数', '请至少勾选一个导出字段', 'error');
            return;
        }

        try {
            const response = await fetch('/users/export/local', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({field_list: fieldList})
            });

            if (response.ok) {
                showToast('任务已提交', '异步导出后台线程已启动，请查看右侧监控器状态', 'success');
                await loadExportTasks();
            } else {
                showToast('提交失败', '提交异步导出失败', 'error');
            }
        } catch (err) {
            showToast('提交失败', err.message, 'error');
        }
    }

    // 状态文案和样式
    function getStatusBadge(status) {
        let label = '排队中';
        if (status === 1) label = '导出中';
        if (status === 2) label = '成功';
        if (status === 3) label = '失败';
        return `<span class="status-badge status-${status}">${label}</span>`;
    }

    // 加载导出任务列表
    async function loadExportTasks() {
        try {
            const response = await fetch('/export-tasks');
            if (!response.ok) {
                console.error('刷新任务列表失败');
                return;
            }
            const result = await response.json();

            if (result.code === 200) {
                const tasks = result.data['exportTaskList'];
                const tbody = document.getElementById('task-table-body');
                tbody.innerHTML = '';

                if (tasks.length === 0) {
                    tbody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:var(--text-muted);">暂无导出记录</td></tr>`;
                    return;
                }

                tasks.forEach(task => {
                    const createTime = task.create_time ? task.create_time.replace('T', ' ') : '-';
                    const fileSizeLabel = task.file_size ? formatBytes(task.file_size) : '-';

                    let actionButton = '-';
                    if (task.export_status === 2) {
                        actionButton = `
                                <div class="btn-group" style="justify-content: center; display: inline-flex;">
                                    <button class="btn btn-primary btn-sm" onclick="downloadExportFile(${task.id})">下载</button>
                                    <button class="btn btn-danger btn-sm" onclick="deleteExportTask(${task.id})">删除</button>
                                </div>
                            `;
                    } else if (task.export_status === 3 && task.fail_reason) {
                        actionButton = `
                                <div class="btn-group" style="justify-content: center; display: inline-flex;">
                                    <button class="btn btn-secondary btn-sm" onclick="alert('错误原因:\\n${task.fail_reason.replace(/'/g, "\\'")}')">查看错误</button>
                                    <button class="btn btn-danger btn-sm" onclick="deleteExportTask(${task.id})">删除</button>
                                </div>
                            `;
                    } else if (task.export_status === 1) {
                        actionButton = `<span class="spinner"></span>`;
                    }

                    const row = document.createElement('tr');
                    row.innerHTML = `
                            <td style="font-weight:600; color:var(--primary)">${task.id}</td>
                            <td>${getStatusBadge(task.export_status)}</td>
                            <td>${fileSizeLabel}</td>
                            <td style="font-size:0.75rem; color:var(--text-secondary)">${createTime}</td>
                            <td>${actionButton}</td>
                        `;
                    tbody.appendChild(row);
                });
            }
        } catch (err) {
            console.error(err);
        }
    }

    // 下载异步导出文件
    const downloadingTasks = new Set();
    async function downloadExportFile(taskId) {
        if (!taskId) {
            showToast('下载失败', '未指定有效的任务ID', 'error');
            return;
        }

        if (downloadingTasks.has(taskId)) {
            showToast('下载中', '该文件正在拉取下载中，请稍候...', 'info');
            return;
        }

        downloadingTasks.add(taskId);
        showToast('开始下载', '正在拉取导出文件，请稍候...', 'info');
        try {
            const response = await fetch(`/export-tasks/${taskId}/download`);
            if (!response.ok) {
                try {
                    const errJson = await response.json();
                    showToast('下载失败', errJson.msg || ('下载失败: HTTP ' + response.status), 'error');
                } catch (_) {
                    showToast('下载失败', '文件下载失败: HTTP ' + response.status, 'error');
                }
                return;
            }

            // 从响应头 Content-Disposition 中提取后端生成的友好中文文件名
            let fileName = '用户数据.xlsx';
            const disposition = response.headers.get('content-disposition');
            if (disposition) {
                const utf8Match = disposition.match(/filename\*=UTF-8''([^;]+)/i);
                if (utf8Match && utf8Match[1]) {
                    fileName = decodeURIComponent(utf8Match[1]);
                } else {
                    const normalMatch = disposition.match(/filename="?([^";]+)"?/i);
                    if (normalMatch && normalMatch[1]) {
                        fileName = decodeURIComponent(normalMatch[1]);
                    }
                }
            }

            const blob = await response.blob();
            const blobUrl = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = blobUrl;
            a.download = fileName;
            document.body.appendChild(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(blobUrl);
            showToast('下载完成', `文件 ${fileName} 已成功保存到本地`, 'success');
        } catch (err) {
            showToast('下载失败', err.message || '网络请求异常', 'error');
        } finally {
            downloadingTasks.delete(taskId);
        }
    }

    // 删除失败的导出任务
    async function deleteExportTask(id) {
        if (!confirm(`确定要删除任务 ${id} 吗？`)) return;
        try {
            const response = await fetch(`/export-tasks/${id}`, {method: 'DELETE'});
            const result = await response.json();
            if (result.code === 200) {
                showToast('删除成功', `任务 ${id} 已被删除`, 'success');
                await loadExportTasks();
            } else {
                showToast('操作失败', result.msg || '删除失败', 'error');
            }
        } catch (err) {
            showToast('操作失败', err.message, 'error');
        }
    }

    // 定时轮询器
    setInterval(() => {
        const autoRefresh = document.getElementById('auto-refresh').checked;
        if (autoRefresh) {
            loadExportTasks();
        }
    }, 5000);

    let tagsArray = [];

    function renderTags() {
        const container = document.getElementById('hobbies-tags-container');
        const inputField = document.getElementById('tags-input-field');
        if (!container || !inputField) return;
        container.querySelectorAll('.tag-badge').forEach(b => b.remove());
        tagsArray.forEach((tag, index) => {
            const badge = document.createElement('span');
            badge.className = 'tag-badge';
            badge.innerHTML = `${tag} <span class="remove-tag" onclick="removeTag(${index})">&times;</span>`;
            container.insertBefore(badge, inputField);
        });
        document.getElementById('add-hobbies').value = tagsArray.join(',');
    }

    function addTag(tagText) {
        tagText = tagText.trim();
        if (tagText && !tagsArray.includes(tagText)) {
            tagsArray.push(tagText);
            renderTags();
        }
    }

    window.removeTag = function (index) {
        tagsArray.splice(index, 1);
        renderTags();
    };

    function initTagsInput() {
        const container = document.getElementById('hobbies-tags-container');
        const inputField = document.getElementById('tags-input-field');
        if (!container || !inputField) return;

        container.addEventListener('click', (e) => {
            if (e.target === container || e.target.classList.contains('tag-badge')) {
                inputField.focus();
            }
        });

        inputField.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') {
                e.preventDefault();
                addTag(inputField.value);
                inputField.value = '';
            } else if (e.key === 'Backspace' && inputField.value === '') {
                tagsArray.pop();
                renderTags();
            }
        });

        inputField.addEventListener('input', () => {
            const val = inputField.value;
            if (val.includes(',') || val.includes('，')) {
                const tags = val.split(/[，,]/);
                tags.forEach(t => addTag(t));
                inputField.value = '';
            }
        });

        inputField.addEventListener('blur', () => {
            addTag(inputField.value);
            inputField.value = '';
        });
    }

    // 初始化加载
    window.onload = () => {
        updateThemeIcons(savedTheme);
        updateLoginStatus();
        loadUsers();
        loadExportTasks();
        initTagsInput();
    }
