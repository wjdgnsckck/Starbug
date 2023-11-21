
import './Sale.css';

import axios from 'axios';
import React, { useState, useEffect } from 'react';

/*   ------------ MUI Table 관련 컨포넌트 ------------   */
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

/*   ------------ MUI 페이징처리 관련 ------------   */
import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';

import excelLogo from '../img/엑셀_로고.png';

// 매출 출력 ==================================================================
export default function SalePrint(props) {

    // 0. 매출정보(주문정보) 담아둘 곳
    //let [ saleList, setSaleList ] = useState([]);

    // 0. 페이징 처리를 위해 컨트롤러에게 전달 받은 객체
    let [ pageDto, setPageDto ] = useState({
        ordersDtoList : [],
        totalPage : 0,
        totalCount : 0

    });

    // 0. 페이징 처리를 위해 컨트롤러에게 전달할 객체
    let [ pageInfo, setPageInfo ] = useState({
        page : 1,
        key : 'mno',
        keyword : '',
        searchOn : false
        }
    );

    // 1. axios 통신 : 매출정보(주문정보) 내역 받아오기
    const getSale = (e) => {
        axios.get("/saleController/doAll", { params : pageInfo })
            .then(r => {
                if (r.data) {
                    //alert('매출 정보 호출');
                    console.log(r.data);
                    setPageDto(r.data);
                }
            });
    }

    // 컴포넌트가 생성될 때 실행된다.
    useEffect ( () => { getSale(); } , [ pageInfo.page, pageInfo.searchOn ] )

    // 2. 페이지 버튼을 클릭 했을때
    const onPageSelect = (e, value) => {
        pageInfo.page = value;
        setPageInfo( {...pageInfo} )
    }

    // 3. 검색 버튼을 클릭 했을때
    const onSearch = (e) => {
        // 페이지를 바꾸다가 검색 했을시 다시 1페이지로 이동 해야함!
        setPageInfo( {...pageInfo, page : 1 , searchOn : true } )
        console.log("pageInfo : ")
        console.log(pageInfo)
    }

    // 4. 검색 제거 버튼 클릭 했을때
    const deleteSearch = (e) => {
        setPageInfo({ ...pageInfo, page: 1, key: 'mno', keyword: '', searchOn : false });
    }

    // 5. 테이블 레코드를 클릭했을때 상세 정보 출력하기
    let [ selectOnoInTable, setSelectOnoInTable ] = useState(0);
    let [ orderDetails, setOrderDetails ] = useState([]);

    console.log("setSelectOnoInTable")
    console.log(selectOnoInTable)

    const selectOno = (e) => {
        axios.get("/saleController/doOne", { params : { ono : selectOnoInTable }})
            .then(r => {
                if (r.data) {
                    console.log("오더 상세 정보 호출");
                    console.log(r.data);
                    setOrderDetails(r.data);
                }
            });
    }
    useEffect ( () => { selectOno(); } , [ selectOnoInTable ] )
    console.log("orderDetails");
    console.log(orderDetails);

    const onCancle = (ono) => {
        console.log("onCancle 입장 ono = "+ono)
        if(window.confirm('환불 하시겠습니까?')){
            axios.put("/pos/cancelOrder",{ ono : ono })
                .then(r => {
                    if (r.data) {
                        alert(ono+"번 주문 환불 완료");
                        console.log(ono+"번 주문 환불 완료");
                        getSale();
                        window.location.reload() // 강제로 재랜더링 하기
                    }
                });
        }
    }

    return (<>
        <div className="saleContainer">
            <div className="salepadding">
            <div className="saleH3"> 매출(주문)내역
            <span> {/*엑셀 다운로드 버튼 공간*/}
                <a href={"/excel/sales"} >
                    <img src={excelLogo} className="excelImgForsales" />
                </a>
            </span>
            {/* 검색 제거 버튼 on/off */}
            { pageInfo.searchOn == false ?
                    '' :
                    <button className="searchBtn OnSearchBtn" onClick={ deleteSearch } type="button"> 검색제거 </button>
            }
            </div>

            {/* ========================= 테이블 구역 =========================*/}
            <div className="saleMuiTableBox">
                <TableContainer component={Paper}>
                  <Table sx={{ minWidth: 1100 }} aria-label="simple table" >
                  {/* 테이블 제목 구역 */}
                    <TableHead className="saleMuiTable">
                      <TableRow>
                        <TableCell align="center"> 주문번호</TableCell>
                        <TableCell align="center"> 주문회원번호</TableCell>
                        <TableCell align="center"> 주문상태</TableCell>
                        <TableCell align="center"> 주문일</TableCell>
                        <TableCell align="center"> 주문금액</TableCell>
                        <TableCell align="center"> 결제금액</TableCell>
                        <TableCell align="center"> 비고 </TableCell>
                      </TableRow>
                    </TableHead>
                    {/* 테이블 내용 구역 */}
                    <TableBody className="saleMuiTable">
                      {pageDto.ordersDtoList.map((row) => (
                        <TableRow
                            className="ordersTableTd"
                            key={row.ono}
                            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                        >
                          <TableCell onClick={ () => {setSelectOnoInTable(row.ono)} } align="center"> {row.ono} </TableCell>
                          <TableCell onClick={ () => {setSelectOnoInTable(row.ono)} } align="center"> { row.mno == 1 ? '비회원' : row.mno } </TableCell>
                          <TableCell onClick={ () => {setSelectOnoInTable(row.ono)} } align="center"> { row.ostate == 0 ? '결제완료' : '환불'} </TableCell>
                          <TableCell onClick={ () => {setSelectOnoInTable(row.ono)} } align="center"> {row.odate.slice(0 , 10)} </TableCell>
                          <TableCell onClick={ () => {setSelectOnoInTable(row.ono)} } align="center"> {row.totalprice.toLocaleString()} </TableCell>
                          <TableCell onClick={ () => {setSelectOnoInTable(row.ono)} } align="center"> {row.paid.toLocaleString()} </TableCell>
                          <TableCell align="center">
                            {row.ostate == 1 ? '' :
                            <button className="onCanclebtn"
                                onClick={ () => onCancle(row.ono) } type="button"> 환불 </button>}
                           </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </TableContainer>
            </div> {/* 테이블 구역 끝*/}

                <div className="PaginationAndSearchBox"> {/* 페이징과 검색 구역 */}
                    {/* 페이징 처리하기 */}
                    <div className="PaginationBox" >
                        <Pagination page = {pageInfo.page} count={pageDto.totalPage}
                                    onChange={onPageSelect} />
                    </div>

                    {/* 검색하기 */}
                    <div className="searchbox">
                        <select
                            value={ pageInfo.key }
                            onChange={ (e) => {
                                setPageInfo({...pageInfo , key : e.target.value })
                            } }
                        >
                            <option value="mno"> 주문회원번호 </option>
                            <option value="ostate"> 주문상태 </option>
                            <option value="odate"> 주문날짜 </option>
                        </select>
                        <input type="text"
                            value={ pageInfo.keyword }
                            onChange={ (e) => {
                                setPageInfo({...pageInfo , keyword : e.target.value })
                            } }
                        />
                        <button className="searchBtn" onClick={ onSearch } type="button"> 검색 </button>

                    </div>

                </div>
            </div>


            {/*  상세 주문 내역 출력 구역 */}

            <div className="saleDetailsBox">
                <div className="salepadding">
                    <div className="saleH3"> 상세 내역 </div>
                        {
                        selectOnoInTable != 0 && orderDetails.length != 0 ?
                        <div className="orderDetailsBox">
                            <div className="orderDetailsTitle">
                                <div> 주문번호 </div> <hr/>
                                <div> 주문자명 </div> <hr/>
                                <div> 주문날짜 </div> <hr/>
                                <div> 주문상태 </div> <hr/>
                                <div> 주문금액 </div> <hr/>
                                <div> 결제금액 </div> <hr/>
                                <div> 주문상품 </div> <hr/>
                            </div>
                            <div className="orderDetailsContent">
                                <div> { orderDetails[0].ono } </div> <hr/>
                                <div> { orderDetails[0].mname } </div> <hr/>
                                <div> { orderDetails[0].odate.slice(0 , 10) } </div> <hr/>
                                <div> { orderDetails[0].ostate == 0 ? '결제완료' : '환불' } </div> <hr/>
                                <div> { orderDetails[0].totalprice.toLocaleString() } </div> <hr/>
                                <div> { orderDetails[0].paid.toLocaleString() } </div> <hr/>
                                <div> {
                                        orderDetails.map( (p) => {
                                            return <div> { p.pname } </div>
                                        })
                                } </div> <hr/>

                            </div>
                        </div>
                        : ''
                        }
                </div>
            </div>

        </div>
    </>)
}