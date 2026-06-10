import React, { useState, useEffect } from 'react';
import axios from 'axios';

const API = 'http://localhost:8080/api';
const INVESTOR_ID = 1;

export default function App() {
  const [portfolio, setPortfolio] = useState(null);
  const [withdrawals, setWithdrawals] = useState([]);
  const [form, setForm] = useState({ productId: '', amount: '' });
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchPortfolio();
    fetchWithdrawals();
  }, []);

  const fetchPortfolio = async () => {
    try {
      const res = await axios.get(
        `${API}/investors/${INVESTOR_ID}/portfolio`);
      setPortfolio(res.data);
    } catch {
      setError('Failed to load portfolio');
    }
  };

  const fetchWithdrawals = async () => {
    try {
      const res = await axios.get(
        `${API}/withdrawals?investorId=${INVESTOR_ID}`);
      setWithdrawals(res.data);
    } catch {
      setError('Failed to load withdrawal history');
    }
  };

  const handleWithdraw = async (e) => {
    e.preventDefault();
    setMessage(null);
    setError(null);

    // UI Validation
    if (!form.productId) {
      return setError('Please select a product');
    }
    if (!form.amount || Number(form.amount) <= 0) {
      return setError('Please enter a valid amount');
    }

    setLoading(true);
    try {
      await axios.post(`${API}/withdrawals`, {
        investorId: INVESTOR_ID,
        productId: Number(form.productId),
        amount: Number(form.amount),
      });
      setMessage('Withdrawal submitted successfully!');
      setForm({ productId: '', amount: '' });
      fetchPortfolio();
      fetchWithdrawals();
    } catch (err) {
      setError(err.response?.data?.error || 'Withdrawal failed');
    } finally {
      setLoading(false);
    }
  };

  const downloadCsv = async () => {
    try {
      const res = await axios.get(
        `${API}/withdrawals/export?investorId=${INVESTOR_ID}`,
        { responseType: 'blob' }
      );
      const url = window.URL.createObjectURL(new Blob([res.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'withdrawals.csv');
      document.body.appendChild(link);
      link.click();
    } catch {
      setError('Failed to download CSV');
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.header}>
        <h1 style={styles.title}>Enviro365 Investments</h1>
        <p style={styles.subtitle}>Investor Portal</p>
      </div>

      {/* Portfolio Dashboard */}
      {portfolio ? (
        <div style={styles.card}>
          <h2 style={styles.cardTitle}>Portfolio Dashboard</h2>
          <div style={styles.investorInfo}>
            <p><strong>Name:</strong> {portfolio.firstName} {portfolio.lastName}</p>
            <p><strong>Email:</strong> {portfolio.email}</p>
            <p><strong>Age:</strong> {portfolio.age}</p>
          </div>
          <div style={styles.grid}>
            {portfolio.products.map(p => (
              <div key={p.id} style={styles.productCard}>
                <div style={styles.productName}>{p.name}</div>
                <span style={{
                  ...styles.badge,
                  background: p.type === 'RETIREMENT' ? '#003366' : '#f5a623'
                }}>
                  {p.type}
                </span>
                <div style={styles.balance}>
                  R {p.balance.toLocaleString('en-ZA', {
                    minimumFractionDigits: 2
                  })}
                </div>
              </div>
            ))}
          </div>
        </div>
      ) : (
        <div style={styles.card}>
          <p style={styles.loading}>Loading portfolio...</p>
        </div>
      )}

      {/* Withdrawal Form */}
      <div style={styles.card}>
        <h2 style={styles.cardTitle}>Submit Withdrawal</h2>
        {message && (
          <div style={styles.success}>{message}</div>
        )}
        {error && (
          <div style={styles.errorMsg}>{error}</div>
        )}
        <div style={styles.form}>
          <select
            value={form.productId}
            onChange={e => setForm({ ...form, productId: e.target.value })}
            style={styles.input}
          >
            <option value="">Select a product...</option>
            {portfolio?.products.map(p => (
              <option key={p.id} value={p.id}>
                {p.name} ({p.type}) - R {p.balance.toLocaleString('en-ZA')}
              </option>
            ))}
          </select>

          <input
            type="number"
            placeholder="Enter withdrawal amount (R)"
            value={form.amount}
            onChange={e => setForm({ ...form, amount: e.target.value })}
            style={styles.input}
            min="1"
          />

          <button
            onClick={handleWithdraw}
            style={{
              ...styles.btn,
              opacity: loading ? 0.7 : 1
            }}
            disabled={loading}
          >
            {loading ? 'Processing...' : 'Submit Withdrawal'}
          </button>
        </div>
      </div>

      {/* Withdrawal History */}
      <div style={styles.card}>
        <div style={styles.tableHeader}>
          <h2 style={styles.cardTitle}>Withdrawal History</h2>
          <button onClick={downloadCsv} style={styles.csvBtn}>
            Download CSV
          </button>
        </div>

        {withdrawals.length === 0 ? (
          <p style={styles.noData}>No withdrawals yet.</p>
        ) : (
          <div style={styles.tableWrapper}>
            <table style={styles.table}>
              <thead>
                <tr style={styles.tableHeadRow}>
                  <th style={styles.th}>ID</th>
                  <th style={styles.th}>Product ID</th>
                  <th style={styles.th}>Amount</th>
                  <th style={styles.th}>Date</th>
                  <th style={styles.th}>Status</th>
                </tr>
              </thead>
              <tbody>
                {withdrawals.map((w, index) => (
                  <tr key={w.id} style={{
                    background: index % 2 === 0 ? '#f9f9f9' : '#fff'
                  }}>
                    <td style={styles.td}>{w.id}</td>
                    <td style={styles.td}>{w.productId}</td>
                    <td style={styles.td}>
                      R {w.amount.toLocaleString('en-ZA', {
                        minimumFractionDigits: 2
                      })}
                    </td>
                    <td style={styles.td}>
                      {new Date(w.withdrawalDate)
                        .toLocaleDateString('en-ZA')}
                    </td>
                    <td style={styles.td}>
                      <span style={{
                        color: w.status === 'APPROVED' ? 'green' : 'red',
                        fontWeight: 'bold'
                      }}>
                        {w.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

const styles = {
  container: {
    maxWidth: 900,
    margin: '0 auto',
    padding: '24px 16px',
    fontFamily: 'Arial, sans-serif',
    background: '#f4f6f9',
    minHeight: '100vh'
  },
  header: {
    background: '#003366',
    color: '#fff',
    padding: '24px',
    borderRadius: 10,
    marginBottom: 24,
    borderBottom: '4px solid #f5a623'
  },
  title: {
    margin: 0,
    fontSize: 28,
    color: '#fff'
  },
  subtitle: {
    margin: '4px 0 0',
    color: '#f5a623',
    fontSize: 14
  },
  card: {
    background: '#fff',
    border: '1px solid #e0e0e0',
    borderRadius: 10,
    padding: 24,
    marginBottom: 24,
    boxShadow: '0 2px 8px rgba(0,0,0,0.06)'
  },
  cardTitle: {
    color: '#003366',
    marginTop: 0,
    borderBottom: '2px solid #f5a623',
    paddingBottom: 8
  },
  investorInfo: {
    background: '#f8f9fa',
    padding: 12,
    borderRadius: 8,
    marginBottom: 16
  },
  grid: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
    gap: 16,
    marginTop: 16
  },
  productCard: {
    background: '#f8f9fa',
    borderRadius: 8,
    padding: 16,
    borderLeft: '4px solid #003366'
  },
  productName: {
    fontWeight: 'bold',
    marginBottom: 8,
    color: '#003366'
  },
  badge: {
    color: '#fff',
    borderRadius: 4,
    padding: '2px 8px',
    fontSize: 11,
    fontWeight: 'bold'
  },
  balance: {
    fontSize: 22,
    fontWeight: 'bold',
    color: '#003366',
    marginTop: 8
  },
  form: {
    display: 'flex',
    flexDirection: 'column',
    gap: 12,
    marginTop: 12
  },
  input: {
    padding: '10px 14px',
    border: '1px solid #ccc',
    borderRadius: 6,
    fontSize: 15,
    width: '100%',
    boxSizing: 'border-box'
  },
  btn: {
    background: '#003366',
    color: '#fff',
    border: 'none',
    borderRadius: 6,
    padding: '12px 0',
    fontSize: 15,
    cursor: 'pointer',
    fontWeight: 'bold'
  },
  csvBtn: {
    background: '#f5a623',
    color: '#fff',
    border: 'none',
    borderRadius: 6,
    padding: '8px 16px',
    cursor: 'pointer',
    fontWeight: 'bold',
    fontSize: 14
  },
  tableHeader: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 16
  },
  tableWrapper: {
    overflowX: 'auto'
  },
  table: {
    width: '100%',
    borderCollapse: 'collapse'
  },
  tableHeadRow: {
    background: '#003366',
    color: '#fff'
  },
  th: {
    padding: '12px 16px',
    textAlign: 'left',
    color: '#fff',
    fontWeight: 'bold'
  },
  td: {
    padding: '10px 16px',
    borderBottom: '1px solid #eee'
  },
  success: {
    color: 'green',
    background: '#e8f5e9',
    padding: '10px 14px',
    borderRadius: 6,
    marginBottom: 12
  },
  errorMsg: {
    color: '#c62828',
    background: '#ffebee',
    padding: '10px 14px',
    borderRadius: 6,
    marginBottom: 12
  },
  loading: {
    color: '#666',
    textAlign: 'center'
  },
  noData: {
    color: '#666',
    fontSize: 14
  }
};