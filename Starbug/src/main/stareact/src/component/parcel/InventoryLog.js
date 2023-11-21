import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

//mui
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
//page
import Pagination from '@mui/material/Pagination';

import Header from '../Header.js';
//css
import styles from './InventoryLog.css';

export default function InventoryLog( props ){
        let [ recouresLog ,  setRecouresLog ] = useState ([])

        const getRecouresLog = (e) =>{
            let inLogDate = document.querySelector('.inLogDate').value;
            console.log(inLogDate)
            axios.get('/inventory/recouresLog',{params : { date: inLogDate}})
            .then( (r)=>{setRecouresLog(r.data)})
        }

        //useEffect( ()=>{ } , [ RecouresLog.inlogdate] );

        return (<>
        <div className="bodyContainer"> {/*전체구역*/}
            <div className = "inLogWrap">
               <div className="saleH3"> 재고로그 </div>
               <div className = "inLogDateSearch">
                   <input className = "inLogDate" type = "date"/>
                   <button className ="logSearchBtn" onClick={getRecouresLog} type="button">검색하기</button>
               </div>
               <table className="logContentArea">
                    <tr className ="inLogContentLine">
                        <td className="invenLogTh" style={{ width : "20%" }}>
                            번호
                        </td>
                        <td className="invenLogTh" style={{ width : "20%" }}>
                            재료명
                        </td>
                        <td className="invenLogTh" style={{ width : "20%" }}>
                            재고
                        </td>
                        <td className="invenLogTh" style={{ width : "20%" }}>
                            재료금액
                        </td>
                        <td className="invenLogTh" style={{ width : "20%" }}>
                            소진량
                        </td>
                    </tr>
                   {
                    recouresLog.map ((row)=>(
                    <tr className="inLogContentLine">
                        <td className="invenLogTd" style={{ width : "20%" }}>
                            {row.resno}
                        </td>
                        <td className="invenLogTd" style={{ width : "20%" }}>
                            {row.resname}
                        </td>
                        <td className="invenLogTd" style={{ width : "20%" }}>
                            {row.rescount}
                        </td>
                        <td className="invenLogTd" style={{ width : "20%" }}>
                            {row.resprice}
                        </td>
                        <td className="invenLogTd" style={{ width : "20%" }}>
                            { row.salecount >= 0 && row.salecount < 1 ?
                            "소량" : row.salecount }
                        </td>
                    </tr>
                    ) )
                   }
               </table>
           </div>
        </div>
        </>)
}