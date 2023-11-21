
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



export default function BuyOutPrint(props) {

    // 발주 =================================================================
    // 0. 페이징 처리를 위해 컨트롤러에게 전달 받은 객체
    let [ pageDtoForPurchase, setPageDtoForPurchase ] = useState({
        parcellogDtos : [],
        totalPage : 0,
        totalCount : 0
    });
    // 0. 페이징 처리를 위해 컨트롤러에게 전달할 객체
    let [ pageInfoForPurchase, setPageInfoForPurchase ] = useState({
        page : 1, key : 'resname', keyword : '', view : 10, searchOn : false }
    );

    // 급여 =================================================================
    // 0. 페이징 처리를 위해 컨트롤러에게 전달 받은 객체
    let [ pageDtoForSalary, setPageDtoForSalary ] = useState({ // 급여
        salaryDtos : [],
        totalPage : 0,
        totalCount : 0
    });
    // 0. 페이징 처리를 위해 컨트롤러에게 전달할 객체
    let [ pageInfoForSalary, setPageInfoForSalary ] = useState({ // 급여
        page : 1, key : 'sname', keyword : '', view : 10, searchOn : false }
    );
    //  =====================================================================


            useEffect(() => {
                // 1. PurchasePrint 데이터 로딩 ----------------------------
                const fetchPurchaseData = async () => {
                    const response = await axios.get("/PurchaseForSales/doAllPurchase", { params : pageInfoForPurchase });
                    if (response.data) {
                      console.log(response.data);
                      setPageDtoForPurchase(response.data);
                    }
                };
                // 1. SalaryPrint 데이터 로딩 ----------------------------
                const fetchSalaryData = async () => {
                    const response = await axios.get("/PurchaseForSales/doAllSalary", { params : pageInfoForSalary });
                    if (response.data) {
                      console.log(response.data);
                      setPageDtoForSalary(response.data);
                    }
                }; fetchSalaryData(); fetchPurchaseData();
            }, [  pageInfoForPurchase.page, pageInfoForPurchase.searchOn,
                pageInfoForSalary.page, pageInfoForSalary.searchOn ]);

                console.log("purchaseData ★ ")
                console.log(pageDtoForPurchase)
                console.log("salaryData ★ ")
                console.log(pageDtoForSalary)

    // 2. 페이지 버튼을 클릭 했을때 --------------------------------------------------------
    // 발주
    const onPageSelectForPurchase = (e, value) => {
        pageInfoForPurchase.page = value;
        setPageInfoForPurchase( {...pageInfoForPurchase} )
    }
    // 급여
    const onPageSelectForSalary = (e, value) => {
        pageInfoForSalary.page = value;
        setPageInfoForSalary( {...pageInfoForSalary} )
    }
    // 3. 검색 버튼을 클릭 했을때 // 페이지를 바꾸다가 검색 했을시 다시 1페이지로 이동 해야함! ------
    // 발주
    const onSearchForPurchase = (e) => {
        setPageInfoForPurchase( {...pageInfoForPurchase, page : 1 , searchOn : true } )
    }
    // 급여
    const onSearchForSalary = (e) => {
        setPageInfoForSalary( {...pageInfoForSalary, page : 1 , searchOn : true } )
    }

    // 4. 검색 제거 버튼 클릭 했을때 --------------------------------------------------------
    // 발주
    const deleteSearchForPurchase = (e) => {
        setPageInfoForPurchase({ ...pageInfoForPurchase, page: 1, key: 'resname', keyword: '', searchOn : false });
    }
    // 급여
    const deleteSearchForSalary = (e) => {
        setPageInfoForSalary({ ...pageInfoForSalary, page: 1, key: 'sname', keyword: '', searchOn : false });
    }

    return (<>
        <div className="saleContainer">



            {/* 발주 테이블 */}
            <div className="purchaseBox0102"> {/* 발주 */}
                <div className="saleH3"> 발주 내역
                    {/* 검색 제거 버튼 on/off */}
                    { pageInfoForPurchase.searchOn ?
                    ( <button className="searchBtn OnSearchBtn"
                        onClick={deleteSearchForPurchase} type="button" > 검색제거 </button> ) :
                        ( '' )}
                </div> {/* 발주 내역 제목 end */}
                <div> {/* ========================= 발주 테이블+페이징+검색 구역 =========================*/}
                    <div className="purchaseMuiTableBox">
                        <div className="purchaseMuiTableBox_01">
                            <TableContainer component={Paper}>
                              <Table sx={{ minWidth: 740 }} aria-label="simple table" >
                              {/* 테이블 제목 구역 */}
                                <TableHead className="purchaseMuiTable">
                                  <TableRow>
                                    <TableCell align="center"> 날짜 </TableCell>
                                    <TableCell align="center"> 주문품목 </TableCell>
                                    <TableCell align="center"> 수량 </TableCell>
                                    <TableCell align="center"> 가격 </TableCell>
                                    <TableCell align="center"> 총 금액 </TableCell>
                                  </TableRow>
                                </TableHead>
                                {/* 테이블 내용 구역 */}
                                <TableBody className="saleMuiTable">
                                  {pageDtoForPurchase.parcellogDtos.map((row) => (
                                    <TableRow key={row.name} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                                      <TableCell align="center"> {row.cdate.slice(0 , 10)} </TableCell>
                                      <TableCell align="center"> {row.resname} </TableCell>
                                      <TableCell align="center"> {row.pacount} </TableCell>
                                      <TableCell align="center"> {row.resprice.toLocaleString()} </TableCell>
                                      <TableCell align="center"> {(row.pacount*row.resprice).toLocaleString()} </TableCell>
                                    </TableRow>
                                  ))}
                                </TableBody>
                              </Table>
                            </TableContainer>
                        </div>
                    </div> {/* 테이블 구역 끝*/}

                    <div className="PaginationAndSearchBox"> {/* 페이징과 검색 구역 */}
                        {/* 페이징 처리하기 */}
                        <div className="PaginationBox" >
                            <Pagination page = {pageInfoForPurchase.page} count={pageDtoForPurchase.totalPage}
                                        onChange={onPageSelectForPurchase} />
                        </div>

                        {/* 검색하기 */}
                        <div className="searchbox">
                            <select
                                value={ pageInfoForPurchase.key }
                                onChange={ (e) => {
                                    setPageInfoForPurchase ({...pageInfoForPurchase , key : e.target.value })
                                } }
                            >
                                <option value="resname"> 발주품목 </option>
                                <option value="cdate"> 발주날짜 </option>
                            </select>
                            <input type="text"
                                value={ pageInfoForPurchase.keyword }
                                onChange={ (e) => {
                                    setPageInfoForPurchase({...pageInfoForPurchase , keyword : e.target.value })
                                } }
                            />
                            <button className="searchBtn" onClick={ onSearchForPurchase } type="button"> 검색 </button>
                        </div> {/* 검색하기 end */}
                    </div> {/* 페이징과 검색 구역 end */}
                </div> {/* ========================= 발주 테이블+페이징+검색 구역 =========================*/}
            </div> {/* 발주 end */}




            {/* 급여테이블 */}
            <div className="purchaseBox0102"> {/* 급여 */}
                <div className="saleH3"> 급여 내역
                    
                    {/* 검색 제거 버튼 on/off */}
                    { pageInfoForSalary.searchOn ?
                    ( <button className="searchBtn OnSearchBtn"
                        onClick={deleteSearchForSalary} type="button" > 검색제거 </button> ) :
                        ( '' )}
                </div> {/* 급여 내역 제목 end */}
                <div> {/* ========================= 급여 테이블+페이징+검색 구역 =========================*/}
                    <div className="purchaseMuiTableBox">
                        <div className="purchaseMuiTableBox_02">
                            <TableContainer component={Paper}>
                              <Table sx={{ minWidth: 740 }} aria-label="simple table" >
                              {/* 테이블 제목 구역 */}
                                <TableHead className="purchaseMuiTable">
                                  <TableRow>
                                    <TableCell align="center"> 날짜 </TableCell>
                                    <TableCell align="center"> 사원번호 </TableCell>
                                    <TableCell align="center"> 지급액 </TableCell>
                                  </TableRow>
                                </TableHead>
                                {/* 테이블 내용 구역 */}
                                <TableBody className="saleMuiTable">
                                  {pageDtoForSalary.salaryDtos.map((row) => (
                                    <TableRow key={row.name} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                                      <TableCell align="center"> {row.sdate.slice(0 , 10)} </TableCell>
                                      <TableCell align="center"> {row.sname} </TableCell>
                                      <TableCell align="center"> {row.sbasepay.toLocaleString()} </TableCell>
                                    </TableRow>
                                  ))}
                                </TableBody>
                              </Table>
                            </TableContainer>
                        </div>
                    </div> {/* 테이블 구역 끝*/}

                    <div className="PaginationAndSearchBox"> {/* 페이징과 검색 구역 */}
                        {/* 페이징 처리하기 */}
                        <div className="PaginationBox" >
                            <Pagination page = {pageInfoForSalary.page} count={pageDtoForSalary.totalPage}
                                        onChange={onPageSelectForSalary} />
                        </div>

                        {/* 검색하기 */}
                        <div className="searchbox">
                            <select
                                value={ pageInfoForSalary.key }
                                onChange={ (e) => {
                                    setPageInfoForSalary({...pageInfoForSalary , key : e.target.value })
                                } }
                            >
                                <option value="sname"> 사원명 </option>
                                <option value="sdate"> 지급날짜 </option>
                            </select>
                            <input type="text"
                                value={ pageInfoForSalary.keyword }
                                onChange={ (e) => {
                                    setPageInfoForSalary({...pageInfoForSalary , keyword : e.target.value })
                                } }
                            />
                            <button className="searchBtn"  onClick={ onSearchForSalary } type="button"> 검색 </button>
                        </div> {/* 검색하기 end */}
                    </div> {/* 페이징과 검색 구역 end */}
                </div> {/* ========================= 급여 테이블+페이징+검색 구역 end=========================*/}
            </div> {/* 급여 end */}
        </div>
    </>);
}