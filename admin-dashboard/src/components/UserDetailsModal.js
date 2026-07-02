// Arquivo: src/components/UserDetailsModal.js
import React from 'react';
import { Modal, View, Text, TouchableOpacity, StyleSheet } from 'react-native';

export default function UserDetailsModal({ visible, usuario, onFechar }) {
  if (!usuario) return null;

  const isAtivo = usuario.status === 'ativo';

  return (
    <Modal transparent visible={visible} animationType="fade" onRequestClose={onFechar}>
      <TouchableOpacity style={styles.overlay} activeOpacity={1} onPress={onFechar}>
        <TouchableOpacity activeOpacity={1} style={styles.card}>
          <Text style={styles.titulo}>Detalhes do Usuário</Text>

          <View style={styles.linha}>
            <Text style={styles.rotulo}>Nome</Text>
            <Text style={styles.valor}>{usuario.name}</Text>
          </View>

          <View style={styles.linha}>
            <Text style={styles.rotulo}>E-mail</Text>
            <Text style={styles.valor}>{usuario.email || '—'}</Text>
          </View>

          <View style={styles.linha}>
            <Text style={styles.rotulo}>Status</Text>
            <View style={styles.statusCell}>
              <View style={[styles.statusDot, { backgroundColor: isAtivo ? '#4CAF50' : '#C0392B' }]} />
              <Text style={styles.valor}>{isAtivo ? 'Ativo' : 'Inativo'}</Text>
            </View>
          </View>

          <View style={styles.linha}>
            <Text style={styles.rotulo}>Cadastro</Text>
            <Text style={styles.valor}>{usuario.cadastro}</Text>
          </View>

          <View style={styles.linha}>
            <Text style={styles.rotulo}>Livros cadastrados</Text>
            <Text style={styles.valor}>{usuario.livros}</Text>
          </View>

          <View style={styles.linha}>
            <Text style={styles.rotulo}>Estantes secretas</Text>
            <Text style={styles.valor}>{usuario.estantes}</Text>
          </View>

          <TouchableOpacity
            onPress={onFechar}
            style={styles.botaoFechar}
            accessibilityRole="button"
            accessibilityLabel="Fechar detalhes do usuário"
          >
            <Text style={styles.textoFechar}>Fechar</Text>
          </TouchableOpacity>
        </TouchableOpacity>
      </TouchableOpacity>
    </Modal>
  );
}

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.35)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  card: {
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    borderWidth: 2,
    borderColor: '#BC86C4',
    paddingVertical: 28,
    paddingHorizontal: 32,
    minWidth: 340,
    maxWidth: 420,
    gap: 14,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.15,
    shadowRadius: 8,
    elevation: 8,
  },
  titulo: {
    fontSize: 18,
    fontWeight: '700',
    color: '#2F3036',
    marginBottom: 6,
  },
  linha: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  rotulo: {
    fontSize: 13,
    color: '#7A7A7A',
  },
  valor: {
    fontSize: 14,
    color: '#2F3036',
    fontWeight: '500',
  },
  statusCell: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
  },
  statusDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
  },
  botaoFechar: {
    marginTop: 12,
    alignSelf: 'flex-end',
    paddingVertical: 8,
    paddingHorizontal: 16,
  },
  textoFechar: {
    color: '#BC86C4',
    fontWeight: '600',
    fontSize: 15,
  },
});