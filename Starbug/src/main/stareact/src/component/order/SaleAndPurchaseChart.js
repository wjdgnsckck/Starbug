
import './Sale.css';


// recharts 라이브러리 사용
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from "recharts";
import { PieChart, Pie, Cell, Label } from "recharts";

import axios from 'axios';
import React, { useState, useEffect } from 'react';

// 차트 출력 ==================================================================
export default function Chart (props) {
// name: ??, uv: ??, pv: ??, amt: ??
//    const dataForSale = [
//      { name: "1월", 매출: 4000, 매입: 2400 },
//      { name: "2월", 매출: 3000, 매입: 1398 },
//      { name: "3월", 매출: 2000, 매입: 9800 },
//      { name: "4월", 매출: 2780, 매입: 3908 },
//      { name: "5월", 매출: 1890, 매입: 4800 },
//      { name: "6월", 매출: 2390, 매입: 3800 },
//      { name: "7월", 매출: 3490, 매입: 4300 },
//      { name: "8월", 매출: 3490, 매입: 4300 },
//      { name: "9월", 매출: 13000, 매입: 4300 },
//      { name: "10월", 매출: 3490, 매입: 4300 },
//      { name: "11월", 매출: 3490, 매입: 4300 },
//      { name: "12월", 매출: 3490, 매입: 10000 }
//    ];

    // 상태 변수
      // 받아온 데이터
      const [dataForSale, setDataForSale] = useState([]);
      const [dataForProduct, setDataForProduct] = useState([]);
      // 매입매출 선택 날짜
      const [salesDate, setSalesDate] = useState("2023"); console.log("salesDate : " + salesDate);
      //상품 카테고리 선택 날짜
      const [startDate, setStartDate] = useState("2023-01-01"); console.log("startDate : " + startDate);
      const [endDate, setEndDate] = useState("2023-12-31"); console.log("endDate : " + endDate);



    // 날짜 기준 차트 데이터 가져오기 ( 통신 두번이 한번에 이루어져야 함 !!! )
    const onChart = (e) => {
        axios.get('/saleController/getChartForProduct', {
            params: { startDate: startDate, endDate: endDate }})
          .then((r) => {
            console.log('상품 카테고리별 데이터');
            console.log(r.data);
            setDataForProduct(
              r.data.map((item) => ({
                name: item.pcname,
                value: item.percentage
              }))
            );
          });
        axios
          .get('/saleController/getChartForSales', { params: { salesDate : salesDate } })
          .then((r) => {
            console.log('매입매출 월별 데이터');
            console.log(r.data);
            let sData = r.data.sdata; console.log(sData);
            let bData = r.data.bdata; console.log(bData);
            //    [dataForSale, setDataForSale] | { name: "1월", 매출: 4000, 매입: 2400 },
            const mappedData = sData.map((item, index) => ({
              name: item.month,
              매출: item.sales,
              매입: bData[index].purchase
            }));
            setDataForSale(mappedData);
            console.log("dataForSale ▼")
            console.log(dataForSale)
          });
    }

      // 컴포넌트가 렌더링될 때 실행
      useEffect(() => { onChart(); }, []);
      const onSearchChart = (e) => { onChart(); }

    ////////////////////////////////////////////////////////////////////////
    const COLORS = ["#286873", "#883C3C", "#A29A39", "#588360", "#634697" ];
    const RADIAN = Math.PI / 180;
    const renderCustomizedLabel = ({
      cx,
      cy,
      midAngle,
      innerRadius,
      outerRadius,
      percent,
      index
    }: any) => {
      const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
      const x = cx + radius * Math.cos(-midAngle * RADIAN);
      const y = cy + radius * Math.sin(-midAngle * RADIAN);

    return (
        <text
          x={x}
          y={y}
          fill="white"
          textAnchor={x > cx ? "start" : "end"}
          dominantBaseline="central"
        >
          {`${dataForProduct[index].name} ${(percent * 100).toFixed(0)}%`}
        </text>
      );
    };
    return (<>

    <div className="chartContainer"> {/* 전체 구역 */}

        <div style={{ display:"flex", justifyContent: "space-around"}}>
            <div className="rechartsDateBox"> {/* 매입매출 차트 출력 날짜 선택 */}
                <input className="chartsDateInput"
                    type="text"
                    value={salesDate}
                    onChange={(e) => setSalesDate(e.target.value)}
                />
                <button className="searchBtn" onClick={ onSearchChart } type="button"> 검색 </button>
            </div>{/* 매입매출 차트 출력 날짜 선택 end*/}


            <div className="rechartsDateBox"> {/* 상품 차트 출력 날짜 선택 */}
                <input className="chartsDateInput"
                    type="date"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                />
                <span style = {{fontWeight: "bold", fontSize: "18px"}}> ~ </span>
                <input className="chartsDateInput"
                    type="date"
                    value={endDate}
                    onChange={(e) => setEndDate(e.target.value)}
                />
                <button className="searchBtn" onClick={ onSearchChart } type="button"> 검색 </button>
            </div>{/* 상품 차트 출력 날짜 선택 end*/}
        </div>

        <div className="rechartsBox"> {/* 차트 구역 */}
            <div className="salepadding"> {/* 매입매출 비교 차트 */}
                <div className="saleH3"> 매입매출 비교 차트 <span className="saleH3span"> 매입내역은 발주와 급여 합산 금액이며 매출은 실 결제 금액이 반영되어 있습니다.</span> </div>
                <LineChart width={750} height={600} data={dataForSale}
                  margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
                >
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Line type="monotone" dataKey="매입" stroke="#8884d8" activeDot={{ r: 15 }} />
                  <Line type="monotone" dataKey="매출" stroke="#82ca9d" />
                </LineChart>
            </div> {/* 매입매출 비교 차트 end */}


            <div className="salepadding"> {/* 상품별 차트 end */}
                <div className="saleH3"> 상품카테고리별 차트 <span className="saleH3span"> 매출이 일어나지 않은 시점을 검색 하였을 시 차트가 출력되지 않습니다. </span> </div>
                <PieChart width={650} height={600}>
                      <Pie
                        data={dataForProduct}
                        cx={380}
                        cy={300}
                        labelLine={false}
                        label={renderCustomizedLabel}
                        outerRadius={230}
                        fill="#8884d8"
                        dataKey="value"
                      >
                        {dataForProduct.map((entry, index) => (
                          <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                        ))}

                      </Pie>
                </PieChart>
            </div> {/* 상품별 차트 end */}


        </div> {/* 차트 구역 end */}
    </div> {/* 전체 구역 */}
    </>);
}