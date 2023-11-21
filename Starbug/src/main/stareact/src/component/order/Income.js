

import './Sale.css';
import './Pos.css';

import axios from 'axios';
import React, { useState, useEffect } from 'react';





// 순이익 출력 ==================================================================
export default function Income(props) {

    const [startDate, setStartDate] = useState("2023-01-01"); console.log("IncomeStartDate : " + startDate);
    const [endDate, setEndDate] = useState("2023-12-31"); console.log("IncomeEndDate : " + endDate);

    const [incomeData, setIncomeData] = useState({
        매출수익: 0,
        매출외수익: 0,
        제품원가: 0,
        매출할인: 0,
        직원급여: 0,
        영업외비용: 0,
        부가세차감전순이익: 0,
        부가세액: 0,
        기간내순이익: 0
    });

    const getIncome = (e) => {
        axios.get("/incomeController/getIncome",{ params: { startDate: startDate, endDate: endDate } })
            .then(r => {
                if (r.data) {
                    console.log(" 손익계산서 자료 ");
                    console.log(r.data);
                    setIncomeData(r.data);
                }
            });
    }; useEffect(() => { getIncome(); }, [  ]);

    // 날짜 변경 후 재 랜더링하기
    const onSearch = (e) => { getIncome(); }




    return (<>
        <div className="saleContainer">
            <div className="salepadding">
                <div style={{ display:"flex"}}>
                    <div className="saleH3"> 간단 손익계산서  </div>



                    <div style= {{paddingLeft: "750px" }}> {/* 출력 날짜 선택 */}
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
                        <button className="searchBtn" onClick={ onSearch } type="button"> 검색 </button>
                    </div>{/* 출력 날짜 선택 end*/}
                </div>
                <div className="incomeCenter" id="pdfContent">
                    <div className="incomeInfo">
                        <span> 커피프린스 1호점 </span>
                        <span> {startDate} ~ {endDate} </span>
                        <span> (단위 : 천원) </span>
                    </div>
                    <div className="incomeInfo_2" >
                        <div> 과목 </div> <div> 금액 </div>
                    </div>
                    <div className="incomeBox">
                        <div className="incomeTitleBox"> {/* 계정과목 부분 */}
                            <h4> 수익 </h4>
                                <h5> 매출수익 </h5>
                                <h5> 매출외수익 </h5>

                            <h4> 비용 </h4>
                                <h5> 제품원가 </h5>
                                <h5> 매출할인 </h5>
                                <h5> 직원급여 </h5>
                                <h5> 영업외비용 </h5>
                            <h4> 부가세 차감전 순이익 </h4>
                                <h5> 부가세액 </h5>
                            <h4 style = {{ color:"#D31616"}} > 기간 내 순이익 </h4>
                        </div>

                        <div className="incomeContentBox"> {/* 금액 부분*/}
                            <div>
                                <h4> (+) </h4>
                                    <h5> {incomeData.매출수익.toLocaleString()} </h5>
                                    <h5> {incomeData.매출외수익.toLocaleString()} </h5>

                                <h4> (-) </h4>
                                    <h5> {incomeData.제품원가.toLocaleString()} </h5>
                                    <h5> {incomeData.매출할인.toLocaleString()} </h5>
                                    <h5> {incomeData.직원급여.toLocaleString()} </h5>
                                    <h5> {incomeData.영업외비용.toLocaleString()} </h5>
                            </div>
                            <div style={{ borderLeft: "2.3px solid #E0E0E0" , paddingTop: "357px" }}>
                                <h4> {incomeData.부가세차감전순이익.toLocaleString()} </h4>
                                    <h5> {incomeData.부가세액.toLocaleString()} </h5>
                                <h4 style = {{ color:"#D31616"}}> {incomeData.기간내순이익.toLocaleString()} </h4>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </>);
}