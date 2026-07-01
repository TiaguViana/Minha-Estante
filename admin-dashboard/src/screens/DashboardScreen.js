// Arquivo: src/screens/DashboardScreen.js
import React, { useState } from 'react';
import { StyleSheet, View, ScrollView, Text } from 'react-native';
import MetricCard from '../components/MetricCard';
import HeaderAdmin from '../components/HeaderAdmin';
import UserTable from '../components/UserTable';
import { colors, typography } from '../styles';

// Dados mockados — quando o Firebase entrar, isso vem de um hook
// (ex: useDashboardMetrics e useUserList) e essa constante some daqui.
const MOCK_USERS = Array(7).fill(null).map((_, i) => ({
  id: String(i + 1),
  name: 'Sabrina Lopes',
  status: 'ativo',         // 'ativo' | 'inativo'
  cadastro: '10/05/2026',
  livros: '45',
  estantes: '45',
}));

export default function DashboardScreen() {
  // Lista de usuários em estado local -- quando vier do Firebase,
  // esse useState vira o retorno de um hook (ex: const { users } = useUserList())
  const [users, setUsers] = useState(MOCK_USERS);

  // Paginação -- quando vier do Firebase, currentPage vai junto na query
  const [currentPage, setCurrentPage] = useState(1);

  // Desativa um usuário: muda o status no estado local.
  // Quando o Firebase entrar, aqui vai um updateDoc no Firestore:
  //   await updateDoc(doc(db, 'usuarios', id), { status: 'inativo' })
  function desativarUsuario(id) {
    setUsers(prev =>
      prev.map(u => u.id === id ? { ...u, status: 'inativo' } : u)
    );
  }

  return (
    <ScrollView style={styles.container} bounces={false}>
      <HeaderAdmin />

      <View style={styles.contentLayout}>
        <Text style={styles.mainTitle}>Gerenciamento de Usuários</Text>

        {/* Cards de métricas */}
        <View style={styles.metricsRow}>
          <MetricCard
            variant="purple"
            icon={require('../../assets/icons/lucide_users.png')}
            value="102"
            label="Usuários no Sistema"
          />
          <MetricCard
            variant="green"
            icon={require('../../assets/icons/tabler_book.png')}
            value="10.00"
            label="Livros Cadastrados"
          />
          <MetricCard
            variant="gold"
            icon={require('../../assets/icons/book.png')}
            tintIcon={false}
            value="30"
            label="Estantes Secretas Ativas"
          />
        </View>

        {/* Tabela de usuários */}
        <UserTable
          data={users}
          currentPage={currentPage}
          totalPages={88}
          totalRegistros={231}
          pageSize={10}
          onPageChange={setCurrentPage}
          onDesativar={desativarUsuario}
          trashIcon={require('../../assets/icons/trash.png')}
        />
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.background,
    width: '100%',
  },
  contentLayout: {
    paddingHorizontal: 80,
    marginTop: 40,
    paddingBottom: 60,
  },
  mainTitle: {
    ...typography.h2,
    color: colors.textbrown,
    marginBottom: 35,
  },
  metricsRow: {
    flexDirection: 'row',
    gap: 36,
    marginBottom: 40,
  },
});