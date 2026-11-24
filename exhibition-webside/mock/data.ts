// 原型用的模拟数据，字段与后端实体一致。数量按真实运营规模准备，方便检查分页、长标题截断、已结束状态等。
// 标注「新增约定」的字段和接口后端还没有，由 Issue #5 按这里的结构实现。

const areaRows: [string, string, string, string][] = [
  ['AS', '亚洲', 'CN', '中国'],
  ['AS', '亚洲', 'JP', '日本'],
  ['AS', '亚洲', 'VN', '越南'],
  ['AS', '亚洲', 'TH', '泰国'],
  ['AS', '亚洲', 'ID', '印度尼西亚'],
  ['AS', '亚洲', 'IN', '印度'],
  ['AS', '亚洲', 'AE', '阿联酋'],
  ['AS', '亚洲', 'SA', '沙特阿拉伯'],
  ['EU', '欧洲', 'DE', '德国'],
  ['EU', '欧洲', 'FR', '法国'],
  ['EU', '欧洲', 'IT', '意大利'],
  ['EU', '欧洲', 'ES', '西班牙'],
  ['EU', '欧洲', 'GB', '英国'],
  ['EU', '欧洲', 'RU', '俄罗斯'],
  ['EU', '欧洲', 'PL', '波兰'],
  ['NA', '北美洲', 'US', '美国'],
  ['NA', '北美洲', 'MX', '墨西哥'],
  ['NA', '北美洲', 'CA', '加拿大'],
  ['SA', '南美洲', 'BR', '巴西'],
  ['SA', '南美洲', 'CL', '智利'],
  ['AF', '非洲', 'ZA', '南非'],
  ['AF', '非洲', 'EG', '埃及'],
  ['AF', '非洲', 'NG', '尼日利亚'],
  ['AF', '非洲', 'KE', '肯尼亚'],
  ['OC', '大洋洲', 'AU', '澳大利亚']
]

export const areas = areaRows.map(
  ([continentCode, continentName, countryCode, countryName], i) => ({
    id: i + 1,
    continentCode,
    continentName,
    countryCode,
    countryName
  })
)

const areaId = (countryName: string) => areas.find(a => a.countryName === countryName)!.id

// [展会名, 国家, 城市/场馆, 开始日期, 结束日期, 类型, 是否主推]
const exhibitionRows: [string, string, string, string, string, string, boolean][] = [
  [
    '德国慕尼黑国际太阳能技术博览会 Intersolar Europe',
    '德国',
    '慕尼黑国际展览中心',
    '2026-06-23',
    '2026-06-25',
    'ENERGY',
    true
  ],
  [
    '德国汉诺威工业博览会 HANNOVER MESSE',
    '德国',
    '汉诺威展览中心',
    '2026-04-20',
    '2026-04-24',
    'ENERGY',
    false
  ],
  [
    '德国杜塞尔多夫国际医疗器械及设备展览会 MEDICA',
    '德国',
    '杜塞尔多夫展览中心',
    '2026-11-16',
    '2026-11-19',
    'MEDICAL',
    true
  ],
  [
    '德国奥格斯堡国际电梯展览会 interlift',
    '德国',
    '奥格斯堡展览中心',
    '2027-10-19',
    '2027-10-22',
    'ELEVATOR',
    true
  ],
  [
    '法国巴黎国际能源与环保展览会 Pollutec',
    '法国',
    '巴黎北维勒班展览中心',
    '2026-10-13',
    '2026-10-16',
    'ENERGY',
    false
  ],
  [
    '意大利米兰国际电梯及电梯配件展览会 Elevator Expo Milano',
    '意大利',
    '米兰国际展览中心',
    '2026-03-17',
    '2026-03-19',
    'ELEVATOR',
    false
  ],
  [
    '意大利维罗纳国际新能源与储能技术展览会（含氢能、光伏、电池及充电基础设施专区）',
    '意大利',
    '维罗纳展览中心',
    '2027-03-09',
    '2027-03-11',
    'ENERGY',
    false
  ],
  [
    '西班牙马德里国际能源与环境展览会 GENERA',
    '西班牙',
    '马德里会展中心',
    '2027-02-23',
    '2027-02-25',
    'ENERGY',
    false
  ],
  [
    '英国伯明翰国际医疗及健康产业博览会',
    '英国',
    '伯明翰国家展览中心',
    '2026-05-12',
    '2026-05-14',
    'MEDICAL',
    false
  ],
  [
    '俄罗斯莫斯科国际电力及电工设备展览会',
    '俄罗斯',
    '莫斯科克洛库斯国际展览中心',
    '2026-12-08',
    '2026-12-11',
    'ENERGY',
    false
  ],
  [
    '俄罗斯莫斯科国际医疗设备展览会 Zdravookhraneniye',
    '俄罗斯',
    '莫斯科中央展览馆',
    '2026-12-01',
    '2026-12-05',
    'MEDICAL',
    false
  ],
  [
    '波兰华沙国际电梯展览会',
    '波兰',
    '华沙国际展览中心',
    '2027-05-18',
    '2027-05-20',
    'ELEVATOR',
    false
  ],
  [
    '越南胡志明市国际电力及新能源展览会',
    '越南',
    '西贡会展中心',
    '2026-08-05',
    '2026-08-07',
    'ENERGY',
    false
  ],
  [
    '越南河内国际医疗医药展览会 Vietnam Medi-Pharm',
    '越南',
    '河内国际展览中心',
    '2027-05-06',
    '2027-05-09',
    'MEDICAL',
    false
  ],
  [
    '泰国曼谷国际可持续能源展览会 ASEW',
    '泰国',
    '曼谷诗丽吉王后国家会议中心',
    '2026-07-01',
    '2026-07-03',
    'ENERGY',
    false
  ],
  [
    '印度尼西亚雅加达国际电梯及建筑技术展览会',
    '印度尼西亚',
    '雅加达国际会展中心',
    '2027-04-14',
    '2027-04-17',
    'ELEVATOR',
    false
  ],
  [
    '印度新德里国际光伏展览会 Intersolar India',
    '印度',
    '新德里国际展览中心',
    '2027-01-20',
    '2027-01-22',
    'ENERGY',
    false
  ],
  [
    '印度孟买国际电梯展览会 IEE Expo',
    '印度',
    '孟买国际展览中心',
    '2027-02-10',
    '2027-02-12',
    'ELEVATOR',
    false
  ],
  [
    '日本东京国际智慧能源周 Smart Energy Week',
    '日本',
    '东京有明国际会展中心',
    '2027-03-03',
    '2027-03-05',
    'ENERGY',
    true
  ],
  [
    '阿联酋迪拜国际医疗设备及器械展览会 Arab Health',
    '阿联酋',
    '迪拜世界贸易中心',
    '2027-01-25',
    '2027-01-28',
    'MEDICAL',
    true
  ],
  [
    '阿联酋迪拜国际中东电力展览会 Middle East Energy',
    '阿联酋',
    '迪拜世界贸易中心',
    '2027-04-06',
    '2027-04-08',
    'ENERGY',
    false
  ],
  [
    '沙特利雅得国际电梯及建筑设备展览会',
    '沙特阿拉伯',
    '利雅得国际会展中心',
    '2026-09-14',
    '2026-09-17',
    'ELEVATOR',
    false
  ],
  [
    '美国拉斯维加斯国际太阳能展览会 RE+',
    '美国',
    '拉斯维加斯威尼斯人会展中心',
    '2026-09-08',
    '2026-09-11',
    'ENERGY',
    false
  ],
  [
    '美国奥兰多国际医疗器械展览会 FIME',
    '美国',
    '奥兰多橙县会议中心',
    '2027-06-16',
    '2027-06-18',
    'MEDICAL',
    false
  ],
  [
    '墨西哥国际电力及新能源展览会',
    '墨西哥',
    '墨西哥城 Centro Citibanamex',
    '2027-06-08',
    '2027-06-10',
    'ENERGY',
    false
  ],
  [
    '加拿大多伦多国际能源展览会',
    '加拿大',
    '多伦多国际会展中心',
    '2027-05-25',
    '2027-05-27',
    'ENERGY',
    false
  ],
  [
    '巴西圣保罗国际电力能源展览会 FIEE',
    '巴西',
    '圣保罗北部展览中心',
    '2027-07-20',
    '2027-07-23',
    'ENERGY',
    false
  ],
  [
    '巴西圣保罗国际医疗器械展览会 Hospitalar',
    '巴西',
    '圣保罗北部展览中心',
    '2027-05-18',
    '2027-05-21',
    'MEDICAL',
    false
  ],
  [
    '智利圣地亚哥国际矿业及能源展览会',
    '智利',
    '圣地亚哥 Espacio Riesco',
    '2027-04-27',
    '2027-04-29',
    'ENERGY',
    false
  ],
  [
    '南非约翰内斯堡国际电力及太阳能展览会 Power & Electricity World Africa',
    '南非',
    '约翰内斯堡加拉格尔会议中心',
    '2027-03-23',
    '2027-03-24',
    'ENERGY',
    false
  ],
  [
    '埃及开罗国际医疗设备展览会 Africa Health ExCon',
    '埃及',
    '开罗国际会议中心',
    '2027-06-01',
    '2027-06-03',
    'MEDICAL',
    false
  ],
  [
    '尼日利亚拉各斯国际电力展览会',
    '尼日利亚',
    '拉各斯 Landmark 会展中心',
    '2027-02-16',
    '2027-02-18',
    'ENERGY',
    false
  ],
  [
    '肯尼亚内罗毕国际医疗健康展览会',
    '肯尼亚',
    '内罗毕肯雅塔国际会议中心',
    '2027-07-07',
    '2027-07-09',
    'MEDICAL',
    false
  ],
  [
    '澳大利亚悉尼国际太阳能及储能展览会 Smart Energy',
    '澳大利亚',
    '悉尼国际会展中心',
    '2027-05-04',
    '2027-05-05',
    'ENERGY',
    false
  ],
  [
    '中国（上海）国际电梯展览会',
    '中国',
    '上海国家会展中心',
    '2026-05-26',
    '2026-05-29',
    'ELEVATOR',
    false
  ]
]

const exhibitionContent = (name: string, location: string, time: string) => `
<h2>展会简介</h2>
<p>${name}是该领域最具影响力的专业展会之一，每届吸引来自全球 60 多个国家和地区的数千家展商、数万名专业观众。展会集中展示行业最新产品与技术，是中国企业开拓当地及周边市场、结识采购商和代理商的重要平台。</p>
<h2>展会信息</h2>
<ul>
  <li>展会时间：${time}</li>
  <li>展会地点：${location}</li>
  <li>主办单位：当地行业协会与国际展览公司联合主办</li>
  <li>中国区代理：雅博展览</li>
</ul>
<h2>展品范围</h2>
<p>整机及系统、核心零部件、配套材料、检测与安装设备、相关软件与服务等。</p>
<h2>参展服务</h2>
<p>雅博展览提供展位预订、特装设计搭建、展品物流、签证邀请函、行程安排及展后跟进等一站式服务，符合条件的企业可申请政府展会补贴。</p>
`

export const exhibitions = exhibitionRows.map(
  ([name, countryName, location, start, end, exhibitionType, featured], i) => {
    const time = `${start} 至 ${end}`
    return {
      id: i + 1,
      name,
      location,
      time,
      year: start.slice(0, 4),
      month: start.slice(5, 7),
      sort: i + 1,
      content: exhibitionContent(name, location, time),
      country: areaId(countryName),
      countryName,
      exhibitionType,
      // 新增约定：主推展会标记、封面图、一句话介绍
      featured,
      coverUrl: 'mock/exhibition.svg',
      summary: `${countryName}${location}举办，雅博展览为中国区代理，提供展位预订与全程参展服务。`
    }
  }
)

export const carousels = [
  { title: '德国电梯展 interlift 中国展团', imageUrl: 'mock/banner-1.svg', link: '/exhibitions/4' },
  {
    title: 'Intersolar Europe 2026 雅博展团',
    imageUrl: 'mock/banner-2.svg',
    link: '/exhibitions/1'
  },
  { title: '一站式出展服务 助力企业开拓国际市场', imageUrl: 'mock/banner-3.svg', link: '' }
].map((item, i) => ({
  id: i + 1,
  ...item,
  sort: i + 1,
  status: 1,
  createTime: '2026-10-01 10:00:00',
  updateTime: '2026-10-01 10:00:00'
}))

const articleContent = (title: string) => `
<p>${title}。本文结合我们近年服务出海企业的经验，整理了参展前需要重点关注的事项。</p>
<h2>一、提前确定目标市场</h2>
<p>根据产品认证、价格带和当地渠道情况选择展会，避免盲目参展。建议提前 6～9 个月锁定展位。</p>
<h2>二、准备好展品与资料</h2>
<p>展品物流通常需要提前 45 天发运；宣传资料、报价单建议准备英文及当地语言版本。</p>
<h2>三、展后及时跟进</h2>
<p>展会结束后一周内是跟进客户的黄金期，按意向等级分批联系，效果最好。</p>
`

const informationTitles = [
  '2027 年海外重点能源展会日历发布',
  '中东电力市场需求持续增长，光伏储能成热点',
  '参加欧洲医疗器械展前，CE 认证需要注意什么？',
  '展位选在哪里更好？标准展位与特装展位对比',
  '东南亚电梯市场观察：越南、印尼新建项目增多',
  '出国参展签证办理全流程与常见问题',
  '展品如何报关运输？ATA 单证册使用指南',
  '一场展会带回 30 个询盘：某储能企业参展复盘',
  '如何在展会上高效接待采购商？',
  '拉美市场开拓：巴西展会参展注意事项汇总，以及当地认证、清关、付款方式等常见问题的详细解答',
  '非洲电力缺口带来的出口机会',
  '海外展会预算怎么做？费用构成详解',
  '展后跟进邮件模板分享',
  '德国展会的展台搭建规范',
  '2026 年度参展企业满意度调查结果'
]

export const informations = informationTitles.map((title, i) => {
  const day = String(28 - i).padStart(2, '0')
  return {
    id: i + 1,
    title,
    content: articleContent(title),
    coverUrl: 'mock/news.svg',
    createTime: `2026-10-${day} 09:30:00`,
    updateTime: `2026-10-${day} 09:30:00`
  }
})

const caseTitles = [
  '某光伏组件企业参展 Intersolar Europe，现场签约 3 家欧洲经销商',
  '电梯配件企业首次出海 interlift，获得 50+ 有效询盘',
  '医疗器械企业参展 Arab Health，打开中东市场',
  '储能企业连续三年参展日本智慧能源周',
  '变压器厂商借助 Middle East Energy 拓展海湾国家',
  '体外诊断企业参展 MEDICA 的特装展位实践',
  '充电桩企业在巴西 FIEE 展会的本地化推广',
  '电梯整机企业印度 IEE Expo 参展复盘',
  '小型家用储能品牌参展澳大利亚 Smart Energy'
]

export const cases = caseTitles.map((title, i) => ({
  id: i + 1,
  title,
  content: `<p>${title}。从展会选择、展位设计、展品物流到展后客户跟进，雅博展览全程协助，帮助客户把展会投入转化为实际订单。</p><p>客户评价：服务专业、响应及时，明年继续合作。</p>`,
  coverUrl: 'mock/case.svg',
  createTime: `2026-0${(i % 9) + 1}-15 10:00:00`
}))

const subsidyTitles = [
  '2027 年境外展会参展补贴申报通知',
  '中小企业国际市场开拓资金申报指南',
  '新能源企业出国参展专项补贴',
  '首次参展企业展位费减免政策',
  '重点展会特装展位补贴说明',
  '跨境电商企业海外推广补贴（含展会、线上推广、海外仓等多项支持内容）'
]

export const subsidies = subsidyTitles.map((title, i) => ({
  id: i + 1,
  title,
  content: `<p>${title}：符合条件的企业参加指定境外展会，可按展位费的一定比例申请补贴，最高不超过规定金额。具体申报条件、材料清单与截止时间请咨询顾问。</p>`,
  price: [50000, 30000, 80000, 20000, 40000, 60000][i],
  startTime: '2026-11-01 00:00:00',
  endTime: '2027-03-31 23:59:59',
  createTime: '2026-11-01 10:00:00'
}))

export const contactInfo = {
  address: '北京市海淀区三里河路9号住建部大院',
  phone: ['010-53630591/2/3', '18310336602'],
  contactPerson: '王经理',
  workingHours: '周一至周五 9:00-18:00',
  holidayArrangement: '法定节假日按国家规定安排，假期可电话或微信联系值班顾问',
  email: 'service@topexpo.com.cn',
  companyProfile:
    '雅博国际展览（天津）有限公司专业从事境外展会的组展工作，为参展企业提供定制化一站式出展服务，助力企业开拓国际市场。\n我们是多个海外知名能源、电梯、医疗展会的中国区代理，提供展位预订、展台设计搭建、签证行程、展品物流与补贴申请等全流程服务。'
}

// 新增约定：多顾问联系人（GET /contact/consultants）
export const consultants = [
  {
    id: 1,
    name: '王经理',
    title: '欧洲展会顾问',
    phone: '138 0000 0001',
    wechat: 'yabo-wang',
    sort: 1
  },
  {
    id: 2,
    name: '李经理',
    title: '中东/非洲展会顾问',
    phone: '138 0000 0002',
    wechat: 'yabo-li',
    sort: 2
  },
  {
    id: 3,
    name: '张经理',
    title: '亚太展会顾问',
    phone: '138 0000 0003',
    wechat: 'yabo-zhang',
    sort: 3
  },
  { id: 4, name: '陈经理', title: '美洲展会顾问', phone: '138 0000 0004', wechat: '', sort: 4 }
]
